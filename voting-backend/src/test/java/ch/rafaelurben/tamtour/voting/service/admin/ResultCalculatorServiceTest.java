package ch.rafaelurben.tamtour.voting.service.admin;

import static org.junit.jupiter.api.Assertions.*;

import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultItemDto;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingPosition;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import java.time.OffsetDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResultCalculatorServiceTest {
  @Autowired private VotingCandidateMapper mapper;
  private ResultCalculatorService service;

  private static VotingCandidate candidateA;
  private static VotingCandidate candidateB;
  private static List<VotingCandidate> candidateList;

  @BeforeAll
  static void initCandidates() {
    candidateA = VotingCandidate.builder().id(1L).name("A").build();
    candidateB = VotingCandidate.builder().id(2L).name("B").build();
    candidateList = new ArrayList<>();
    for (long i = 1; i <= 15; i++) {
      candidateList.add(VotingCandidate.builder().id(i).name("C" + i).build());
    }
  }

  @BeforeEach
  void setup() {
    service = new ResultCalculatorService(mapper);
  }

  // --- compareTo tests ---
  @Test
  void compareTo_aHasMorePointsThanB_aComesFirst() {
    var itemA = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateA));
    var itemB = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateB));
    // A: 12 points
    itemA.addPosition(1);
    assertEquals(12, itemA.getPoints());
    // B: 10 points
    itemB.addPosition(2);
    assertEquals(10, itemB.getPoints());
    // A has more points
    assertTrue(itemA.compareTo(itemB) < 0);
    assertTrue(itemB.compareTo(itemA) > 0);
  }

  @Test
  void compareTo_aHasMorePointsThanB_bHasMoreFirstPlaces_aComesFirst() {
    var itemA = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateA));
    var itemB = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateB));
    // A: 12+10+10=32
    itemA.addPosition(1);
    itemA.addPosition(2);
    itemA.addPosition(2);
    assertEquals(32, itemA.getPoints());
    // B: 12+12+1=25
    itemB.addPosition(1);
    itemB.addPosition(1);
    itemB.addPosition(10);
    assertEquals(25, itemB.getPoints());
    // B has more 1st places (2), but A has more points
    assertTrue(itemA.compareTo(itemB) < 0);
    assertTrue(itemB.compareTo(itemA) > 0);
  }

  @Test
  void compareTo_samePoints_aHasMoreFirstPlaces_aComesFirst() {
    var itemA = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateA));
    var itemB = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateB));
    // A: 12+12=24
    itemA.addPosition(1);
    itemA.addPosition(1);
    assertEquals(24, itemA.getPoints());
    // B: 10+10+4=24
    itemB.addPosition(2);
    itemB.addPosition(2);
    itemB.addPosition(7);
    assertEquals(24, itemB.getPoints());
    // A has the same amount of points, but more 1st places
    assertTrue(itemA.compareTo(itemB) < 0);
    assertTrue(itemB.compareTo(itemA) > 0);
  }

  @Test
  void compareTo_samePointsSameFirstPlaces_aHasMoreSecondPlaces_aComesFirst() {
    var itemA = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateA));
    var itemB = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateB));
    // A: 12+10+10=32
    itemA.addPosition(1);
    itemA.addPosition(2);
    itemA.addPosition(2);
    assertEquals(32, itemA.getPoints());
    // B: 12+10+8+2=32
    itemB.addPosition(1);
    itemB.addPosition(2);
    itemB.addPosition(3);
    itemB.addPosition(9);
    assertEquals(32, itemB.getPoints());
    // Both have same 1st, but A has more 2nd
    assertTrue(itemA.compareTo(itemB) < 0);
    assertTrue(itemB.compareTo(itemA) > 0);
  }

  @Test
  void compareTo_samePointsSameAllPlaces_theyAreEqual() {
    var itemA = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateA));
    var itemB = new ResultCalculatorService.VotingResultItem(mapper.toDto(candidateB));
    // A: 12+10+8=30
    itemA.addPosition(1);
    itemA.addPosition(2);
    itemA.addPosition(3);
    assertEquals(30, itemA.getPoints());
    // B: 12+10+8=30
    itemB.addPosition(1);
    itemB.addPosition(2);
    itemB.addPosition(3);
    assertEquals(30, itemB.getPoints());
    // Both have same points and same positions
    assertEquals(0, itemA.compareTo(itemB));
    assertEquals(0, itemB.compareTo(itemA));
  }

  // --- calculateResult tests ---
  @Test
  void calculateResult_simpleCase() {
    VotingCategory category =
        VotingCategory.builder()
            .id(1L)
            .name("TestCat")
            .submissionEnd(OffsetDateTime.now().minusDays(1))
            .build();
    VotingCandidate c1 = candidateA.toBuilder().votingCategory(category).build();
    VotingCandidate c2 = candidateB.toBuilder().votingCategory(category).build();
    category.setVotingCandidates(new HashSet<>(Arrays.asList(c1, c2)));
    // VotingSet 1: A=1st, B=2nd
    VotingSet set1 = VotingSet.builder().id(1L).votingCategory(category).build();
    set1.setVotingPositions(
        new HashSet<>(
            Arrays.asList(
                VotingPosition.builder()
                    .id(1L)
                    .votingCandidateId(c1.getId())
                    .votingCandidate(c1)
                    .votingSet(set1)
                    .position(1)
                    .build(),
                VotingPosition.builder()
                    .id(2L)
                    .votingCandidateId(c2.getId())
                    .votingCandidate(c2)
                    .votingSet(set1)
                    .position(2)
                    .build())));
    set1.setSubmitted(true);
    set1.setDisqualified(false);
    // VotingSet 2: B=1st, A=2nd
    VotingSet set2 = VotingSet.builder().id(2L).votingCategory(category).build();
    set2.setVotingPositions(
        new HashSet<>(
            Arrays.asList(
                VotingPosition.builder()
                    .id(3L)
                    .votingCandidateId(c1.getId())
                    .votingCandidate(c1)
                    .votingSet(set2)
                    .position(2)
                    .build(),
                VotingPosition.builder()
                    .id(4L)
                    .votingCandidateId(c2.getId())
                    .votingCandidate(c2)
                    .votingSet(set2)
                    .position(1)
                    .build())));
    set2.setSubmitted(true);
    set2.setDisqualified(false);
    category.setVotingSets(new HashSet<>(Arrays.asList(set1, set2)));
    VotingCategoryResultDto result = service.calculateResult(category);
    assertTrue(result.ended());
    assertEquals("TestCat", result.title());
    List<VotingCategoryResultItemDto> items = Arrays.asList(result.items());
    assertEquals(2, items.size());
    // Both should have same points and rank, but check order
    assertEquals(items.get(0).points(), items.get(1).points());
    assertEquals(1, items.get(0).rank());
    assertEquals(2, items.get(1).rank());
  }

  @Test
  void calculateResult_tieCase() {
    VotingCategory category =
        VotingCategory.builder()
            .id(1L)
            .name("TieCat")
            .submissionEnd(OffsetDateTime.now().minusDays(1))
            .build();
    List<VotingCandidate> candidates = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      candidates.add(candidateList.get(i).toBuilder().votingCategory(category).build());
    }
    category.setVotingCandidates(new HashSet<>(candidates));
    // VotingSet 1: C1=1st, C2=2nd, C3=3rd
    VotingSet set1 = VotingSet.builder().id(1L).votingCategory(category).build();
    set1.setVotingPositions(
        new HashSet<>(
            Arrays.asList(
                VotingPosition.builder()
                    .id(1L)
                    .votingCandidateId(candidates.get(0).getId())
                    .votingCandidate(candidates.get(0))
                    .votingSet(set1)
                    .position(1)
                    .build(),
                VotingPosition.builder()
                    .id(2L)
                    .votingCandidateId(candidates.get(1).getId())
                    .votingCandidate(candidates.get(1))
                    .votingSet(set1)
                    .position(2)
                    .build(),
                VotingPosition.builder()
                    .id(3L)
                    .votingCandidateId(candidates.get(2).getId())
                    .votingCandidate(candidates.get(2))
                    .votingSet(set1)
                    .position(3)
                    .build())));
    set1.setSubmitted(true);
    set1.setDisqualified(false);
    // VotingSet 2: C2=1st, C3=2nd, C1=3rd
    VotingSet set2 = VotingSet.builder().id(2L).votingCategory(category).build();
    set2.setVotingPositions(
        new HashSet<>(
            Arrays.asList(
                VotingPosition.builder()
                    .id(4L)
                    .votingCandidateId(candidates.get(1).getId())
                    .votingCandidate(candidates.get(1))
                    .votingSet(set2)
                    .position(1)
                    .build(),
                VotingPosition.builder()
                    .id(5L)
                    .votingCandidateId(candidates.get(2).getId())
                    .votingCandidate(candidates.get(2))
                    .votingSet(set2)
                    .position(2)
                    .build(),
                VotingPosition.builder()
                    .id(6L)
                    .votingCandidateId(candidates.get(0).getId())
                    .votingCandidate(candidates.get(0))
                    .votingSet(set2)
                    .position(3)
                    .build())));
    set2.setSubmitted(true);
    set2.setDisqualified(false);
    // VotingSet 3: C3=1st, C1=2nd, C2=3rd
    VotingSet set3 = VotingSet.builder().id(3L).votingCategory(category).build();
    set3.setVotingPositions(
        new HashSet<>(
            Arrays.asList(
                VotingPosition.builder()
                    .id(7L)
                    .votingCandidateId(candidates.get(2).getId())
                    .votingCandidate(candidates.get(2))
                    .votingSet(set3)
                    .position(1)
                    .build(),
                VotingPosition.builder()
                    .id(8L)
                    .votingCandidateId(candidates.get(0).getId())
                    .votingCandidate(candidates.get(0))
                    .votingSet(set3)
                    .position(2)
                    .build(),
                VotingPosition.builder()
                    .id(9L)
                    .votingCandidateId(candidates.get(1).getId())
                    .votingCandidate(candidates.get(1))
                    .votingSet(set3)
                    .position(3)
                    .build())));
    set3.setSubmitted(true);
    set3.setDisqualified(false);
    category.setVotingSets(new HashSet<>(Arrays.asList(set1, set2, set3)));
    VotingCategoryResultDto result = service.calculateResult(category);
    List<VotingCategoryResultItemDto> items = Arrays.asList(result.items());
    // All should have same points
    assertEquals(3, items.size());
    for (VotingCategoryResultItemDto item : items) {
      assertEquals(30, item.points());
    }
    // The rank should be unique but may be in any order (random)
    Set<Integer> ranks = new HashSet<>();
    for (VotingCategoryResultItemDto item : items) {
      ranks.add(item.rank());
    }
    assertEquals(3, ranks.size());
    assertTrue(ranks.contains(1));
    assertTrue(ranks.contains(2));
    assertTrue(ranks.contains(3));
  }

  @Test
  void calculateResult_manyCandidates() {
    VotingCategory category =
        VotingCategory.builder()
            .id(1L)
            .name("BigCat")
            .submissionEnd(OffsetDateTime.now().minusDays(1))
            .build();
    List<VotingCandidate> candidates = new ArrayList<>();
    for (VotingCandidate votingCandidate : candidateList) {
      candidates.add(votingCandidate.toBuilder().votingCategory(category).build());
    }
    category.setVotingCandidates(new HashSet<>(candidates));
    Set<VotingSet> sets = new HashSet<>();
    for (long s = 1; s <= 5; s++) {
      VotingSet set = VotingSet.builder().id(s).votingCategory(category).build();
      Set<VotingPosition> positions = new HashSet<>();
      for (int p = 1; p <= candidates.size(); p++) {
        positions.add(
            VotingPosition.builder()
                .id(s * 100 + p)
                .votingCandidateId((long) p)
                .votingCandidate(candidates.get(p - 1))
                .votingSet(set)
                .position(p)
                .build());
      }
      set.setVotingPositions(positions);
      set.setSubmitted(true);
      set.setDisqualified(false);
      sets.add(set);
    }
    category.setVotingSets(sets);
    VotingCategoryResultDto result = service.calculateResult(category);
    List<VotingCategoryResultItemDto> items = Arrays.asList(result.items());
    assertEquals(candidateList.size(), items.size());
    // Top 10 should have points, rest 0
    for (int i = 0; i < 10; i++) {
      assertTrue(items.get(i).points() > 0);
    }
    for (int i = 10; i < candidateList.size(); i++) {
      assertEquals(0, items.get(i).points());
    }
  }
}
