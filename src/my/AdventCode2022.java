package my;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Advent of Code 2022: dinmchele implementation
 */
public class AdventCode2022 {

    public static void main(String[] args) throws Throwable {
        System.out.println("********************* DAY 1 *********************");
        day1();
        System.out.println("********************* DAY 2 *********************");
        day2();
        System.out.println("********************* DAY 3 *********************");
        day3();
        System.out.println("********************* DAY 4 *********************");
        day4();
    }

    /**
     * Day 1
     * @throws IOException
     */
    public static void day1() throws IOException {

        File file = new File("src/main/resources/source_day1.txt");
        String input = FileUtils.readFileToString(file, "UTF-8");

        List<Integer> sums = new ArrayList<>();
        sums.add(0);

        Arrays.stream(input.split(System.lineSeparator())).forEach(e -> {
            if ((e.length() > 0)) {
                sums.set(sums.size() - 1, new Integer(e) + sums.get(sums.size() - 1));
            } else {
                sums.add(0);
            }
        });

        Integer maxValue = sums.stream().max(Comparator.naturalOrder()).get();
        System.out.println("MAX SUM:" +maxValue + " INDEX:" + sums.indexOf(maxValue));

        sums.sort(Comparator.reverseOrder());
        System.out.println("MAX 3 SUMs:" + sums.get(0) + " " + sums.get(1) + " " + sums.get(2));
        System.out.println("sum of max 3 SUMs:" + (new Integer(sums.get(0)) + new Integer(sums.get(1)) + new Integer(sums.get(2))));

    }

    /**
     * Day 2 Rocket Paper Scissors
     * @throws IOException
     */
    public static void day2() throws IOException {

        File file = new File("src/main/resources/source_day2.txt");
        String input = FileUtils.readFileToString(file, "UTF-8");

        final Integer[] totalScore = {0};

        Map<String, Integer> values = new HashMap<>();
        values.put("A", 1); //rock
        values.put("B", 2); //paper
        values.put("C", 3); //scissor
        values.put("X", 1); //rock
        values.put("Y", 2); //paper
        values.put("Z", 3); //scissor

        Map<String, Integer> results = new HashMap<>();
        results.put("AX", 3); //draw
        results.put("BX", 0); //lose
        results.put("CX", 6); //win
        results.put("AY", 6); //win
        results.put("BY", 3); //draw
        results.put("CY", 0); //lose
        results.put("AZ", 0); //lose
        results.put("BZ", 6); //win
        results.put("CZ", 3); //draw


        Arrays.stream(input.split("\n")).forEach(e -> {
            String[] row = e.split(" ");
            Integer myValue = values.get(row[1]);
            totalScore[0] += myValue + results.get(row[0] + row[1]);
        });


        System.out.println("Final Score:" + totalScore[0] );

        Map<String, Integer> expectedResults = new HashMap<>();
        expectedResults.put("AX", 0); //lose
        expectedResults.put("BX", 0); //lose
        expectedResults.put("CX", 0); //lose
        expectedResults.put("AY", 3); //draw
        expectedResults.put("BY", 3); //draw
        expectedResults.put("CY", 3); //draw
        expectedResults.put("AZ", 6); //win
        expectedResults.put("BZ", 6); //win
        expectedResults.put("CZ", 6); //win

        Map<String, String> mappedChoice = new HashMap<>();
        mappedChoice.put("A0", "C");
        mappedChoice.put("A3", "A");
        mappedChoice.put("A6", "B");
        mappedChoice.put("B0", "A");
        mappedChoice.put("B3", "B");
        mappedChoice.put("B6", "C");
        mappedChoice.put("C0", "B");
        mappedChoice.put("C3", "C");
        mappedChoice.put("C6", "A");

        totalScore[0] = 0;

        Arrays.stream(input.split("\n")).forEach(e -> {
            String[] row = e.split(" ");
            Integer myValue = values.get(mappedChoice.get(row[0] + expectedResults.get(row[0] + row[1])));
            totalScore[0] +=  myValue + expectedResults.get(row[0] + row[1]);
        });

        System.out.println("Final Score Second round:" + totalScore[0] );

    }

    /**
     * Day 3: Rucksack Reorganization
     * One Elf has the important job of loading all of the rucksacks with supplies for the jungle journey. Unfortunately, that Elf didn't quite follow the packing instructions, and so a few items now need to be rearranged.
     *
     * Each rucksack has two large compartments. All items of a given type are meant to go into exactly one of the two compartments. The Elf that did the packing failed to follow this rule for exactly one item type per rucksack.
     *
     * The Elves have made a list of all of the items currently in each rucksack (your puzzle input), but they need your help finding the errors. Every item type is identified by a single lowercase or uppercase letter (that is, a and A refer to different types of items).
     *
     * The list of items for each rucksack is given as characters all on a single line. A given rucksack always has the same number of items in each of its two compartments, so the first half of the characters represent items in the first compartment, while the second half of the characters represent items in the second compartment.
     *
     * For example, suppose you have the following list of contents from six rucksacks:
     *
     * vJrwpWtwJgWrhcsFMMfFFhFp
     * jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
     * PmmdzqPrVvPwwTWBwg
     * wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
     * ttgJtRGJQctTZtZT
     * CrZsJsPPZsGzwwsLwLmpwMDw
     * The first rucksack contains the items vJrwpWtwJgWrhcsFMMfFFhFp, which means its first compartment contains the items vJrwpWtwJgWr, while the second compartment contains the items hcsFMMfFFhFp. The only item type that appears in both compartments is lowercase p.
     * The second rucksack's compartments contain jqHRNqRjqzjGDLGL and rsFMfFZSrLrFZsSL. The only item type that appears in both compartments is uppercase L.
     * The third rucksack's compartments contain PmmdzqPrV and vPwwTWBwg; the only common item type is uppercase P.
     * The fourth rucksack's compartments only share item type v.
     * The fifth rucksack's compartments only share item type t.
     * The sixth rucksack's compartments only share item type s.
     * To help prioritize item rearrangement, every item type can be converted to a priority:
     *
     * Lowercase item types a through z have priorities 1 through 26.
     * Uppercase item types A through Z have priorities 27 through 52.
     * In the above example, the priority of the item type that appears in both compartments of each rucksack is 16 (p), 38 (L), 42 (P), 22 (v), 20 (t), and 19 (s); the sum of these is 157.
     *
     * Find the item type that appears in both compartments of each rucksack. What is the sum of the priorities of those item types?
     * --- Part Two ---
     * As you finish identifying the misplaced items, the Elves come to you with another issue.
     *
     * For safety, the Elves are divided into groups of three. Every Elf carries a badge that identifies their group. For efficiency, within each group of three Elves, the badge is the only item type carried by all three Elves. That is, if a group's badge is item type B, then all three Elves will have item type B somewhere in their rucksack, and at most two of the Elves will be carrying any other item type.
     *
     * The problem is that someone forgot to put this year's updated authenticity sticker on the badges. All of the badges need to be pulled out of the rucksacks so the new authenticity stickers can be attached.
     *
     * Additionally, nobody wrote down which item type corresponds to each group's badges. The only way to tell which item type is the right one is by finding the one item type that is common between all three Elves in each group.
     *
     * Every set of three lines in your list corresponds to a single group, but each group can have a different badge item type. So, in the above example, the first group's rucksacks are the first three lines:
     *
     * vJrwpWtwJgWrhcsFMMfFFhFp
     * jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
     * PmmdzqPrVvPwwTWBwg
     * And the second group's rucksacks are the next three lines:
     *
     * wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
     * ttgJtRGJQctTZtZT
     * CrZsJsPPZsGzwwsLwLmpwMDw
     * In the first group, the only item type that appears in all three rucksacks is lowercase r; this must be their badges. In the second group, their badge item type must be Z.
     *
     * Priorities for these items must still be found to organize the sticker attachment efforts: here, they are 18 (r) for the first group and 52 (Z) for the second group. The sum of these is 70.
     *
     * Find the item type that corresponds to the badges of each three-Elf group. What is the sum of the priorities of those item types?
     * @throws IOException
     */
    public static void day3() throws IOException {

        File file = new File("src/main/resources/source_day3.txt");
        String input = FileUtils.readFileToString(file, "UTF-8");

        AtomicReference<Integer> sum = new AtomicReference<>(0);
        Arrays.stream(input.split("\n")).forEach(e -> {
            String left = e.substring(0, e.length() / 2 );
            String right = e.substring(e.length() / 2 );
            Set<Character> lChars = new HashSet<>();
            Set<Character> rChars = new HashSet<>();

            for (int i = 0; i < left.length(); i++) {
                lChars.add(left.charAt(i));
            }
            for (int i = 0; i < right.length(); i++) {
                rChars.add(right.charAt(i));
            }

            lChars.retainAll(rChars);

            for (Character character: lChars){
                sum.updateAndGet(v -> {
                    if (character.isLowerCase(character))
                        return v + character.hashCode() - 96;
                    else
                        return v + character.hashCode() - 38;
                }); //deriving value from ascii code
            }

        });

        System.out.println("Final Score Third round:" + sum);

        //PART 2
        sum.set(0);
        List<List<String>> groupedLines = new ArrayList<>();
        AtomicInteger cycleCount = new AtomicInteger();
        List<String> singleGroup = new ArrayList<>();
        Arrays.stream(input.split("\n")).forEach(e -> {
           cycleCount.getAndIncrement();
           singleGroup.add(e);
           if (cycleCount.get() > 2) {
               groupedLines.add((List<String>) ((ArrayList<String>) singleGroup).clone());
               cycleCount.set(0);
               singleGroup.removeAll(singleGroup);
           }

        });

        groupedLines.forEach(v -> {
            Set<Character> first = new HashSet<>();
            Set<Character> second = new HashSet<>();
            Set<Character> third = new HashSet<>();

            for (int i = 0; i < v.get(0).length(); i++) {
                first.add(v.get(0).charAt(i));
            }
            for (int i = 0; i < v.get(1).length(); i++) {
                second.add(v.get(1).charAt(i));
            }
            for (int i = 0; i < v.get(2).length(); i++) {
                third.add(v.get(2).charAt(i));
            }

            first.retainAll(second);
            first.retainAll(third);

            for (Character character: first){
                sum.updateAndGet(c -> {
                    if (character.isLowerCase(character))
                        return c + character.hashCode() - 96;
                    else
                        return c + character.hashCode() - 38;
                }); //deriving value from ascii code
            }

        });

        System.out.println("Final Score Third round - part 2:" + sum);


    }

    /**
     * Day 4: Camp Cleanup
     * Space needs to be cleared before the last supplies can be unloaded from the ships, and so several Elves have been assigned the job of cleaning up sections of the camp. Every section has a unique ID number, and each Elf is assigned a range of section IDs.
     *
     * However, as some of the Elves compare their section assignments with each other, they've noticed that many of the assignments overlap. To try to quickly find overlaps and reduce duplicated effort, the Elves pair up and make a big list of the section assignments for each pair (your puzzle input).
     *
     * For example, consider the following list of section assignment pairs:
     *
     * 2-4,6-8
     * 2-3,4-5
     * 5-7,7-9
     * 2-8,3-7
     * 6-6,4-6
     * 2-6,4-8
     * For the first few pairs, this list means:
     *
     * Within the first pair of Elves, the first Elf was assigned sections 2-4 (sections 2, 3, and 4), while the second Elf was assigned sections 6-8 (sections 6, 7, 8).
     * The Elves in the second pair were each assigned two sections.
     * The Elves in the third pair were each assigned three sections: one got sections 5, 6, and 7, while the other also got 7, plus 8 and 9.
     * This example list uses single-digit section IDs to make it easier to draw; your actual list might contain larger numbers. Visually, these pairs of section assignments look like this:
     *
     * .234.....  2-4
     * .....678.  6-8
     *
     * .23......  2-3
     * ...45....  4-5
     *
     * ....567..  5-7
     * ......789  7-9
     *
     * .2345678.  2-8
     * ..34567..  3-7
     *
     * .....6...  6-6
     * ...456...  4-6
     *
     * .23456...  2-6
     * ...45678.  4-8
     * Some of the pairs have noticed that one of their assignments fully contains the other. For example, 2-8 fully contains 3-7, and 6-6 is fully contained by 4-6. In pairs where one assignment fully contains the other, one Elf in the pair would be exclusively cleaning sections their partner will already be cleaning, so these seem like the most in need of reconsideration. In this example, there are 2 such pairs.
     *
     * In how many assignment pairs does one range fully contain the other?
     *
     * --- Part Two ---
     * It seems like there is still quite a bit of duplicate work planned. Instead, the Elves would like to know the number of pairs that overlap at all.
     *
     * In the above example, the first two pairs (2-4,6-8 and 2-3,4-5) don't overlap, while the remaining four pairs (5-7,7-9, 2-8,3-7, 6-6,4-6, and 2-6,4-8) do overlap:
     *
     * 5-7,7-9 overlaps in a single section, 7.
     * 2-8,3-7 overlaps all of the sections 3 through 7.
     * 6-6,4-6 overlaps in a single section, 6.
     * 2-6,4-8 overlaps in sections 4, 5, and 6.
     * So, in this example, the number of overlapping assignment pairs is 4.
     *
     * In how many assignment pairs do the ranges overlap?
     * @throws IOException
     */
    public static void day4() throws IOException {

        File file = new File("src/main/resources/source_day4.txt");
        String input = FileUtils.readFileToString(file, "UTF-8");

        AtomicReference<Integer> countPart1 = new AtomicReference<>(0);
        AtomicReference<Integer> countPart2 = new AtomicReference<>(0);
        Arrays.stream(input.split("\n")).forEach(e -> {
            String[] parts = e.split(",");
            Integer minLeft = new Integer(parts[0].split("-")[0]);
            Integer maxLeft = new Integer(parts[0].split("-")[1]);
            Integer minRight = new Integer(parts[1].split("-")[0]);
            Integer maxRight = new Integer(parts[1].split("-")[1]);
            if ((minLeft >= minRight && maxLeft <= maxRight) ||
                    (minRight >= minLeft && maxRight <= maxLeft)){
               countPart1.getAndSet(countPart1.get() + 1);
            }

            if ( (minLeft >= minRight && maxLeft <= maxRight) ||
                 (minLeft <= minRight && maxLeft >= maxRight) ||
                 (minRight >= minLeft && minRight <= maxLeft ) ||
                 (minLeft >= minRight && minLeft <= maxRight)
            ){
                countPart2.getAndSet(countPart2.get() + 1);
            }

        });

        System.out.println("Final count day 4: " + countPart1);
        System.out.println("Final count day 4 - part 2: " + countPart2);

    }

    /**
     * --- Day 5: Supply Stacks ---
     * The expedition can depart as soon as the final supplies have been unloaded from the ships. Supplies are stored in stacks of marked crates, but because the needed supplies are buried under many other crates, the crates need to be rearranged.
     *
     * The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps. After the crates are rearranged, the desired crates will be at the top of each stack.
     *
     * The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.
     *
     * They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:
     *
     *     [D]
     * [N] [C]
     * [Z] [M] [P]
     *  1   2   3
     *
     * move 1 from 2 to 1
     * move 3 from 1 to 3
     * move 2 from 2 to 1
     * move 1 from 1 to 2
     * In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.
     *
     * Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is moved from one stack to a different stack. In the first step of the above rearrangement procedure, one crate is moved from stack 2 to stack 1, resulting in this configuration:
     *
     * [D]
     * [N] [C]
     * [Z] [M] [P]
     *  1   2   3
     * In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time, so the first crate to be moved (D) ends up below the second and third crates:
     *
     *         [Z]
     *         [N]
     *     [C] [D]
     *     [M] [P]
     *  1   2   3
     * Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:
     *
     *         [Z]
     *         [N]
     * [M]     [D]
     * [C]     [P]
     *  1   2   3
     * Finally, one crate is moved from stack 1 to stack 2:
     *
     *         [Z]
     *         [N]
     *         [D]
     * [C] [M] [P]
     *  1   2   3
     * The Elves just need to know which crate will end up on top of each stack; in this example, the top crates are C in stack 1, M in stack 2, and Z in stack 3, so you should combine these together and give the Elves the message CMZ.
     *
     * After the rearrangement procedure completes, what crate ends up on top of each stack?
     * @throws IOException
     */
    public static void day5() throws IOException {

    }


}
