package com.jezh.textsaver.repository;

import com.jezh.textsaver.configuration.BasePostgresConnectingTest;
import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.Assert.assertTrue;


public class TextPartRepositoryPostgresTest extends BasePostgresConnectingTest {

    private TextPart textPart;
    @Autowired
    private TextPartRepository textPartRepository;
    @Autowired
    private TextCommonDataRepository textCommonDataRepository;
    // an alternative to the standard JPA EntityManager that is specifically designed for tests:
    @Autowired
    private TestEntityManager testEntityManager;
    
    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder()
                .body("TextPartRepositoryPostgresTest/")
                .textCommonData(textCommonDataRepository.findAll().get(0))
                .nextItem((long)(Math.random() * 10000))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        textPart = null;
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(textPartRepository.saveAndFlush(textPart));
        System.out.println("*****************************************************************" + textPart.getId());
    }

    @Test
    public void testCreateFiveItems() {
        for (int i = 0; i < 5; i++) {
            textPart = null;
            textPart = TextPart.builder()
                    .body("TextPartRepositoryPostgresTest/")
                    .textCommonData(textCommonDataRepository.findAll().get(0))
                    .nextItem((long)(Math.random() * 10000))
                    .build();
            textPartRepository.saveAndFlush(textPart);
        }
    }

    @Test
    public void testRetrieveOne() {
        textPart = textPartRepository.findAll().get(0);
        Assert.assertEquals(textPart.getBody(), textPartRepository.findById(textPart.getId()).orElse(new TextPart()).getBody());
    }

    @Test
    public void testUpdateOne() {
        textPart = textPartRepository.findAll().get(0);
        textPart.setBody(textPart.getBody() + " updated");
        textPartRepository.saveAndFlush(textPart);
    }

// ------------------------------------------------------------------------------------------------------------------
// FIXME: в общем, все это нужно положить в @BeforeClass, упаковать все тесты в единую транзакцию, а в @AfterClass сделать полный rollback
    /**
     * Auxiliary operations: assign firstItem in any textCommonData as the random textPart.id from db, then assign
     * foreign key for each element in text_parts table
     * Then set an order of textParts by assigning of textPart.nextItem that binding with each other
     * */
// NB: only 1 running of this test is available without full updating of text_parts table: constraint UNIQUE
// on the column next_item
    @Test
    public void assignFirstItemForAnyTextPartFromDb_thenAssignChosenTextCommonDataId() {
        List<TextPart> textParts = textPartRepository.findAll();
        textParts.forEach(textPart -> textPart.setNextItem(null));
        TextCommonData textCommonData = textCommonDataRepository.findAll().get(0);
        Long firstItem = TestUtil.assignFirstItem(textCommonData, textParts);
        System.out.println("************************************************* firstItem: " + firstItem);
        textCommonDataRepository.saveAndFlush(textCommonData);
        Assert.assertNotNull(textCommonDataRepository.findById(textCommonData.getId()).get().getFirstItem());
        Assert.assertEquals(textCommonData.getFirstItem(),
                textCommonDataRepository.findById(textCommonData.getId()).get().getFirstItem());

        textParts = TestUtil.assignTextPartsForeignKey(textCommonData, textParts);

        Assert.assertEquals(textCommonData.getId(), textParts.stream().findAny().get().getTextCommonData().getId());
// TODO: почему без этой записи все равно происходит обновление всех textPart в бд?
//        textParts.forEach(textPart -> textPartRepository.saveAndFlush(textPart));
        textParts = TestUtil.setTextPartsNextItemOrder(textParts, firstItem);
        TestUtil.assignTextPartBodyToTransferHelpfulData(textParts);

    }

// ------------------------------------------------------------------------------------------------------------------
    @Test
    public void testUpdateForeignKey() {
//        textPart = textPartRepository.findAll().get(0);
        TextPart textPart = textPartRepository.findById(5L).get();
//        textPart.setTextCommonData(null);
        textPart.setTextCommonData(textCommonDataRepository.findAll().get(0));
        textPartRepository.saveAndFlush(textPart);
    }

    @Test
//    @Transactional
    public void testFindByTextCommonDataName() {
        List<TextPart> textPartList = textPartRepository.findByTextCommonDataName("eighth");
        textPartList.forEach(textPart1 -> System.out.println("id: " + textPart1.getId() +
        ", foreignKey: " + textPart1.getTextCommonData().getId() + ": " + textPart1.getTextCommonData().getName()));
    }

    @Test
    public void testFindAllByTextCommonDataId() {
        Long textCommonDataForeignKeyId = textPartRepository.findAll().stream().findAny().get().getTextCommonData().getId();
        System.out.println("*****************************" + textCommonDataForeignKeyId);
        List<TextPart> allById = textPartRepository.findAllByTextCommonDataId(textCommonDataForeignKeyId);
        if (allById.isEmpty()) testFindAllByTextCommonDataId();
        else
            allById.forEach(System.out::println);
    }

    @Test
    public void testFindSortedSetByTextCommonDataId() {
        Long textCommonDataForeignKeyId =
                ((TextPart) TestUtil.getRandomElementFromArray(textPartRepository.findAll().toArray()))
                        .getTextCommonData().getId();
        List<TextPart> sortedList = textPartRepository.findSortedSetByTextCommonDataId(textCommonDataForeignKeyId);
        sortedList.forEach(textPart -> System.out.printf("id: %d, nextItem: %d;\n", textPart.getId(), textPart.getNextItem()));
        // for the last element of sortedList test condition won't be met, as its nextItem don't refer some next textPart
        for (int i = 0; i < sortedList.size() - 1; i++) {
            Assert.assertEquals(sortedList.get(i + 1).getId(), sortedList.get(i).getNextItem());
        }
    }

    @Test
    public void testFindSortedTextPartsByTextCommonDataId() {
        Long textCommonDataForeignKeyId =
                ((TextPart) TestUtil.getRandomElementFromArray(textPartRepository.findAll().toArray()))
                        .getTextCommonData().getId();
//                36L;
        Page<TextPart> page = textPartRepository.findPageByDocDataId(textCommonDataForeignKeyId,
                PageRequest.of(0, 13));
        page.forEach(textPart1 -> System.out.printf("id: %d, nextItem: %d;\n", textPart1.getId(), textPart1.getNextItem()));
        List<TextPart> sortedList = page.getContent();
        for (int i = 0; i < sortedList.size() - 1; i++) {
            Assert.assertEquals(sortedList.get(i + 1).getId(), sortedList.get(i).getNextItem());
        }
    }

    @Test
    public void testFindSortedTextPartBunchByStartId() {
        Long textCommonDataForeignKeyId = textPartRepository.findAll().stream().findAny().get().getTextCommonData().getId();
        System.out.println("*****************************" + textCommonDataForeignKeyId);
        List<TextPart> allById = textPartRepository.findAllByTextCommonDataId(textCommonDataForeignKeyId);
        Long anyTextPartId = ((TextPart) TestUtil.getRandomElementFromArray(allById.toArray())).getId();
        List<TextPart> sortedBunch = textPartRepository.findSortedTextPartBunchByStartId(anyTextPartId, 10);
        sortedBunch.forEach(textPart -> System.out.printf("id: %d, nextItem: %d;\n", textPart.getId(), textPart.getNextItem()));
        // for the sortedBunch containing only 1 element test condition won't be met, as its nextItem don't refer some next textPart
        if (sortedBunch.size() > 1) {
            for (int i = 0; i < sortedBunch.size() - 1; i++) {
                Assert.assertEquals(sortedBunch.get(i + 1).getId(), sortedBunch.get(i).getNextItem());
            }
        }
    }

    //    @Test(expected = DataIntegrityViolationException.class)
//    public void testPreviousItemUniqueConstraint() {
//        TextPart lastSavedTextPart = textPartRepository.findAll().get((int) textPartRepository.count() - 1);
//        textPart.setPreviousItem(lastSavedTextPart.getPreviousItem());
//        textPartRepository.saveAndFlush(textPart);
//    }
//

//@Test
//public void testFindNextByCurrentInSequence() {
//    textPart = textPartRepository.findByTextCommonDataName("eighth").stream().findAny().get();
//    System.out.println("********************************** previous textPart: previousItem = " +
//            textPart.getPreviousItem() + ", nextItem = " + textPart.getNextItem());
////    Long nextItem = textPart.getNextItem();
//    TextPart nextTextPart = textPartRepository.findNextByCurrentInSequence(textPart).get();
//    System.out.println("********************************** next textPart: previousItem = " +
//            nextTextPart.getPreviousItem() + ", nextItem = " + nextTextPart.getNextItem());
//}


//    @Test
//    public void testIndexPreviousItems() {
//        textPartRepository.indexPreviousItems();
//    }
//
//
//    @Test
//    public void testDropIndexPreviousItems() {
//        textPartRepository.dropIndexPreviousItems();
//    }
}