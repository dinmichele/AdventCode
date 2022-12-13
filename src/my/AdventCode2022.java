package my;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

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
        System.out.println("********************* DAY 5 *********************");
        day5();
        System.out.println("********************* DAY 6 *********************");
        day6();
        System.out.println("********************* DAY 7 *********************");
        day7();
        System.out.println("********************* DAY 8 *********************");
        day8();
        System.out.println("********************* DAY 9 *********************");
        day9();
    }

    /**
     * Day 1
     * @throws IOException
     */
    public static void day1() throws IOException {


        List<String> input = Files.readAllLines(Paths.get("resources/source_day1.txt"));

        List<Integer> sums = new ArrayList<>();
        sums.add(0);

        input.forEach(e -> {
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

        List<String> input = Files.readAllLines(Paths.get("resources/source_day2.txt"));

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


        input.forEach(e -> {
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

        input.forEach(e -> {
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

        List<String> input = Files.readAllLines(Paths.get("resources/source_day3.txt"));

        AtomicReference<Integer> sum = new AtomicReference<>(0);
        input.forEach(e -> {
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
        input.forEach(e -> {
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

        List<String> input = Files.readAllLines(Paths.get("resources/source_day4.txt"));

        AtomicReference<Integer> countPart1 = new AtomicReference<>(0);
        AtomicReference<Integer> countPart2 = new AtomicReference<>(0);
        input.forEach(e -> {
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
        List<String> input = Files.readAllLines(Paths.get("resources/source_day5.txt"));

        List<Stack<String>> stacks = new ArrayList<>();
        /*
                            [B]     [L]     [S]
                    [Q] [J] [C]     [W]     [F]
                [F] [T] [B] [D]     [P]     [P]
                [S] [J] [Z] [T]     [B] [C] [H]
                [L] [H] [H] [Z] [G] [Z] [G] [R]
            [R] [H] [D] [R] [F] [C] [V] [Q] [T]
            [C] [J] [M] [G] [P] [H] [N] [J] [D]
            [H] [B] [R] [S] [R] [T] [S] [R] [L]
             1   2   3   4   5   6   7   8   9
         */
        stacks.add(new Stack<>()); stacks.get(0).addAll(List.of(new String[]{"H","C","R"}));
        stacks.add(new Stack<>()); stacks.get(1).addAll(List.of(new String[]{"B", "J", "H", "L", "S", "F"}));
        stacks.add(new Stack<>()); stacks.get(2).addAll(List.of(new String[]{"R", "M", "D", "H", "J", "T", "Q"}));
        stacks.add(new Stack<>()); stacks.get(3).addAll(List.of(new String[]{"S", "G", "R", "H", "Z", "B", "J"}));
        stacks.add(new Stack<>()); stacks.get(4).addAll(List.of(new String[]{"R", "P", "F", "Z", "T", "D", "C","B"}));
        stacks.add(new Stack<>()); stacks.get(5).addAll(List.of(new String[]{"T", "H", "C", "G"}));
        stacks.add(new Stack<>()); stacks.get(6).addAll(List.of(new String[]{"S", "N", "V", "Z", "B", "P", "W","L"}));
        stacks.add(new Stack<>()); stacks.get(7).addAll(List.of(new String[]{"R", "J", "Q", "G", "C"}));
        stacks.add(new Stack<>()); stacks.get(8).addAll(List.of(new String[]{"L", "D", "T", "R", "H", "P", "F","S"}));

        /*
            [D]
        [N] [C]
        [Z] [M] [P]
         1   2   3
         */
        /*stacks.add(new Stack<>()); stacks.get(0).addAll(List.of(new String[]{"Z", "N"}));
        stacks.add(new Stack<>()); stacks.get(1).addAll(List.of(new String[]{"M", "C", "D"}));
        stacks.add(new Stack<>()); stacks.get(2).addAll(List.of(new String[]{"P"}));*/
        input.forEach(l -> {
            String[] elements = l.split(" ");
            Integer index = new Integer(elements[3]);
            Integer moves = new Integer(elements[1]);
            Integer target = new Integer(elements[5]);
            String invertedMoving = "";
            for (int i=0; i< moves; i++) {
               // stacks.get(target -1).push(stacks.get(index -1).pop()); --PART1
                invertedMoving =  stacks.get(index -1).pop() + " " + invertedMoving;
            }
            Arrays.stream(invertedMoving.split(" "))
                    .filter(s -> !(s.equals("") || s.equals("[") || s.equals("]")))
                    .forEach(v -> stacks.get(target -1).push(v)); //--PART 2

        });

        String result = stacks.stream().map(s -> s.pop()).collect(joining());
        System.out.println("Solution Day 5: " + result);


    }

    /**
     * Day 6
     * The preparations are finally complete; you and the Elves leave camp on foot and begin to make your way toward the star fruit grove.
     *
     * As you move through the dense undergrowth, one of the Elves gives you a handheld device. He says that it has many fancy features, but the most important one to set up right now is the communication system.
     *
     * However, because he's heard you have significant experience dealing with signal-based systems, he convinced the other Elves that it would be okay to give you their one malfunctioning device - surely you'll have no problem fixing it.
     *
     * As if inspired by comedic timing, the device emits a few colorful sparks.
     *
     * To be able to communicate with the Elves, the device needs to lock on to their signal. The signal is a series of seemingly-random characters that the device receives one at a time.
     *
     * To fix the communication system, you need to add a subroutine to the device that detects a start-of-packet marker in the datastream. In the protocol being used by the Elves, the start of a packet is indicated by a sequence of four characters that are all different.
     *
     * The device will send your subroutine a datastream buffer (your puzzle input); your subroutine needs to identify the first position where the four most recently received characters were all different. Specifically, it needs to report the number of characters from the beginning of the buffer to the end of the first such four-character marker.
     *
     * For example, suppose you receive the following datastream buffer:
     *
     * mjqjpqmgbljsphdztnvjfqwrcgsmlb
     * After the first three characters (mjq) have been received, there haven't been enough characters received yet to find the marker. The first time a marker could occur is after the fourth character is received, making the most recent four characters mjqj. Because j is repeated, this isn't a marker.
     *
     * The first time a marker appears is after the seventh character arrives. Once it does, the last four characters received are jpqm, which are all different. In this case, your subroutine should report the value 7, because the first start-of-packet marker is complete after 7 characters have been processed.
     *
     * Here are a few more examples:
     *
     * bvwbjplbgvbhsrlpgdmjqwftvncz: first marker after character 5
     * nppdvjthqldpwncqszvftbrmjlhg: first marker after character 6
     * nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg: first marker after character 10
     * zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw: first marker after character 11
     * How many characters need to be processed before the first start-of-packet marker is detected?
     *
     * --- Part Two ---
     * Your device's communication system is correctly detecting packets, but still isn't working. It looks like it also needs to look for messages.
     *
     * A start-of-message marker is just like a start-of-packet marker, except it consists of 14 distinct characters rather than 4.
     *
     * Here are the first positions of start-of-message markers for all of the above examples:
     *
     * mjqjpqmgbljsphdztnvjfqwrcgsmlb: first marker after character 19
     * bvwbjplbgvbhsrlpgdmjqwftvncz: first marker after character 23
     * nppdvjthqldpwncqszvftbrmjlhg: first marker after character 23
     * nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg: first marker after character 29
     * zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw: first marker after character 26:
     *
     * How many characters need to be processed before the first start-of-message marker is detected?
     * @throws IOException
     */
    public static void day6() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("resources/source_day6.txt"));

        int responseValue = identifyPacketStarterPosition(input.get(0).toCharArray(), 4,0);

        System.out.println("Solution Day 6:" + responseValue);

        responseValue = identifyPacketStarterPosition(input.get(0).toCharArray(), 14,0);

        System.out.println("Solution Day 6 - Part2 :" + responseValue);

    }

    private static int identifyPacketStarterPosition(char[] chars, int tokeSize, int start){
        Set<Character> singlePacketValue = new HashSet<>();
        for (int i=0; i<tokeSize; i++) {
            singlePacketValue.add(chars[start + i]);
        }

        if (singlePacketValue.size() < tokeSize) {
            return identifyPacketStarterPosition(chars, tokeSize, start + 1);
        } else {
            return start + tokeSize;
        }
    }

    static int lastVisitedPosition = 1;
    static String dirName = "";
    public static void day7() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("resources/source_day7.txt"));

        Map<String, Integer> dirSizes = new HashMap<>();

        while (lastVisitedPosition < input.size()) {
            determineFolderSize (input, dirName, 0,  dirSizes);
        }

        List sortedKeys=new ArrayList(dirSizes.keySet());
        Collections.sort(sortedKeys);

        AtomicReference<Integer> result = new AtomicReference<>(0);
        sortedKeys.forEach(k -> {
            if (dirSizes.get(k) <= 100000){
                result.updateAndGet(v -> v + dirSizes.get(k));
            }
        });

        //FIXME: it works on the example, but it doesn't work on full input
        System.out.println("Solution Day 7 - Num of directories: " +  result);
    }

    private static int determineFolderSize(List<String> input, String dirName, int size, Map<String, Integer> dirSizes){

        if (lastVisitedPosition >= input.size()){
            lastVisitedPosition++;
            return size;
        }

        if (input.get(lastVisitedPosition).startsWith("$ ls")) {
            lastVisitedPosition++;
            return size + determineFolderSize(input, dirName, size, dirSizes);
        }

        if (input.get(lastVisitedPosition).startsWith("dir") ){

            lastVisitedPosition++;
            return size + determineFolderSize(input, dirName, size, dirSizes);
        } else {
            if (input.get(lastVisitedPosition).startsWith("$ cd .")){
                dirSizes.put(dirName, size);
                lastVisitedPosition++;
                return size;
            } else {
                if (input.get(lastVisitedPosition).startsWith("$ cd")){

                    Matcher matcher = Pattern.compile(".* (\\w+)").matcher(input.get(lastVisitedPosition));
                    matcher.find();
                    dirName +=  "/" + matcher.group(1);
                    lastVisitedPosition++;
                    size = size + determineFolderSize(input, dirName, size, dirSizes);
                    dirSizes.put(dirName, size);
                    return size;
                } else {
                    Matcher matcher = Pattern.compile("\\d+").matcher(input.get(lastVisitedPosition));
                    matcher.find();
                    int singleFileSize = Integer.valueOf(matcher.group());
                    lastVisitedPosition++;
                    return size + singleFileSize + determineFolderSize(input, dirName, size, dirSizes);
                }
            }
        }

        //return size;

    }

    /**
     * --- Day 8: Treetop Tree House ---
     * The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.
     *
     * First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.
     *
     * The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:
     *
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.
     *
     * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
     *
     * All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:
     *
     * The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
     * The top-middle 5 is visible from the top and right.
     * The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
     * The left-middle 5 is visible, but only from the right.
     * The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
     * The right-middle 3 is visible from the right.
     * In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
     * With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.
     *
     * Consider your map; how many trees are visible from outside the grid?
     *
     * Your puzzle answer was 1782.
     *
     * The first half of this puzzle is complete! It provides one gold star: *
     *
     * --- Part Two ---
     * Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.
     *
     * To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)
     *
     * The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
     *
     * In the example above, consider the middle 5 in the second row:
     *
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * Looking up, its view is not blocked; it can see 1 tree (of height 3).
     * Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
     * Looking right, its view is not blocked; it can see 2 trees.
     * Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
     * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
     *
     * However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
     *
     * 30373
     * 25512
     * 65332
     * 33549
     * 35390
     * Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
     * Looking left, its view is not blocked; it can see 2 trees.
     * Looking down, its view is also not blocked; it can see 1 tree.
     * Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
     * This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
     *
     * Consider each tree on your map. What is the highest scenic score possible for any tree?
     */
    public static void day8() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("resources/source_day8.txt"));
        List<List<Integer>> rows = new ArrayList<>();
        input.forEach(l -> {
            String elements[] = l.split("");
            rows.addAll(Collections.singleton(Arrays.stream(elements).map(e -> Integer.valueOf(e)).collect(toList())));

        });

        AtomicInteger rowIndex = new AtomicInteger();

        AtomicInteger sum = new AtomicInteger();
        List<Integer> scenicViews = new ArrayList<>();
        rows.forEach(r -> {
            int columnIndex = 0;

            if (rowIndex.get() > 0 && rowIndex.get() < rows.size() -1){
               for (int i=1; i<r.size() -1; i++) {
                    columnIndex++;
                    List<String> right = new ArrayList<>();
                    List<String> left = new ArrayList<>();
                    List<String> up = new ArrayList<>();
                    List<String> down = new ArrayList<>();
                    for (int j =columnIndex; j<(rows.get(rowIndex.get()).size() - 1); j++){
                        right.add(String.valueOf(rows.get(rowIndex.get()).get(j + 1)));
                    }
                    for (int j=columnIndex; j>0; j--){
                        left.add(String.valueOf(rows.get(rowIndex.get()).get(j - 1)));
                    }
                    for (int k = rowIndex.get() +1; k<(rows.size()); k++){
                        down.add(String.valueOf(rows.get(k).get(columnIndex)));
                    }
                    for (int k = rowIndex.get(); k>0; k--){
                        up.add(String.valueOf(rows.get(k-1).get(columnIndex)));
                    }

                    if (isTreeVisible(r.get(i), right, left, up, down )){
                        sum.getAndIncrement();
                    }

                   scenicViews.add(calculateScenicView(r.get(i), right, left, up, down));


                }

            }
            rowIndex.getAndIncrement();
        });

        int total = sum.get() + (rows.size() *2) + ((rows.get(0).size()-2)*2);
        System.out.println("Day 8 result: " + total);
        System.out.println("Day 8 result - part 2: " + scenicViews.stream().max(Comparator.naturalOrder()));

    }

    private static Integer calculateScenicView(Integer value, List<String> right, List<String> left, List<String> up, List<String> down) {
        int rScore = 0, lScore =0, uScore=0, dScore = 0;
        for (String element:right) {
            if (Integer.valueOf(element)<value) rScore++;
            else  {rScore++; break;}
        }
        for (String element:left) {
            if (Integer.valueOf(element)<value) lScore++;
            else {lScore++; break;}
        }
        for (String element:up) {
            if (Integer.valueOf(element)<value) uScore++;
            else {uScore++; break;}
        }
        for (String element:down) {
            if (Integer.valueOf(element)<value) dScore++;
            else {dScore++; break;}
        }

        return lScore * rScore * dScore * uScore;
    }

    private static boolean isTreeVisible(Integer value, List<String> right, List<String> left, List<String> up, List<String> down) {
        long l = left.stream().filter(e -> Integer.valueOf(e) < value).count();
        long r = right.stream().filter(e -> Integer.valueOf(e) < value).count();
        long u = up.stream().filter(e -> Integer.valueOf(e) < value).count();
        long d = down.stream().filter(e -> Integer.valueOf(e) < value).count();

        return l==left.size() || r==right.size() || u==up.size() || d==down.size();
    }

    /**
     * Day 9
     * @throws IOException
     */
    public static void day9() throws IOException {
        List<String> input = Files.readAllLines(Paths.get("resources/source_day9.txt"));
        List<List<String>> hMatrix = new ArrayList<>();
        List<List<String>> tMatrix = new ArrayList<>();
        List<String> tPositionHistory = new ArrayList<>();
        AtomicInteger hCurrentPosX = new AtomicInteger(0);
        AtomicInteger hCurrentPosY = new AtomicInteger(0);
        AtomicInteger tCurrentPosX = new AtomicInteger(0);
        AtomicInteger tCurrentPosY = new AtomicInteger(0);

        //matrix[hCurrentPosX.get()][hCurrentPosY.get()] = "H"; //start position
        hMatrix.add(hCurrentPosX.get(), Arrays.asList("H")); //start position
        tMatrix.add(hCurrentPosX.get(), Arrays.asList("T")); //T start at same position
        //tPositionHistory.add(tCurrentPosX.get() + " " +tCurrentPosY.get());

        input.forEach(l -> {
            String[] command = l.split(" ");
            String direction = command[0];
            Integer repetition = Integer.valueOf(command[1]);
            for (int i = 0; i < repetition; i++) {
                hMatrix.get(hCurrentPosX.get()).set(hCurrentPosY.get(), null);//remove current H location
                switch (direction){
                    case "R":
                        hCurrentPosY.getAndIncrement();
                        break;
                    case "L":
                        hCurrentPosY.getAndDecrement();
                        break;
                    case "U":
                        hCurrentPosX.getAndIncrement();
                        break;
                    case "D":
                        hCurrentPosX.getAndDecrement();
                        break;
                    default: throw new IllegalStateException("Direction not valid in the command");
                }

                if (hMatrix.size() <= hCurrentPosX.get()){
                    hMatrix.removeAll(hMatrix);
                    hMatrix.addAll((List) Arrays.asList(new ArrayList[hCurrentPosX.get() + 1]));
                    //Arrays.asList(new List[hCurrentPosX.get() + 1]);
                }
                hMatrix.set(hCurrentPosX.get(), Arrays.asList(new String[hCurrentPosY.get() + 1]));
                hMatrix.get(hCurrentPosX.get()).set(hCurrentPosY.get(), "H"); //put H in the new location (After command execution)
                moveTAdjacentToH(tPositionHistory, tMatrix, hCurrentPosX, hCurrentPosY, tCurrentPosX, tCurrentPosY);

            }

        });
        Set<String> uniquePositions = new HashSet<>(tPositionHistory);
        System.out.println("Day 9 result:" + uniquePositions.size());

    }

    private static void moveTAdjacentToH(List<String> tPositionHistory, List<List<String>> tMatrix, AtomicInteger hCurrentPosX, AtomicInteger hCurrentPosY,
                                         AtomicInteger tCurrentPosX, AtomicInteger tCurrentPosY) {

        boolean isAdjacent = Math.abs(hCurrentPosX.get() - tCurrentPosX.get()) < 2 &&
                             Math.abs(hCurrentPosY.get() - tCurrentPosY.get()) < 2;

        if (!isAdjacent) { //move T

           tMatrix.get(tCurrentPosX.get()).set(tCurrentPosY.get(), null);//remove current T location
           Integer newX = tCurrentPosX.get();
           Integer newY = tCurrentPosY.get();

            if (hCurrentPosX.get() - tCurrentPosX.get() > 1) {
                newX++;
            } else if (hCurrentPosX.get() - tCurrentPosX.get() < -1){
                newX--;
            } else  if (hCurrentPosY.get() - tCurrentPosY.get() > 1) {
                newY++;
            } else if (hCurrentPosY.get() - tCurrentPosY.get() < -1) {
                newY--;
            } else {
                // if the head and tail aren't touching and aren't in the same row or column,
                //the tail always moves one step diagonally to keep up
                if (Math.abs(hCurrentPosX.get() - tCurrentPosX.get()) == 2 &&
                        Math.abs(hCurrentPosY.get() - tCurrentPosY.get()) == 1) {
                    if (hCurrentPosX.get() > tCurrentPosX.get() || hCurrentPosY.get() > tCurrentPosY.get()) {
                        newX++; newY++;
                    } else {
                        newX--; newY--;
                    }
                } else {
                    if (Math.abs(hCurrentPosX.get() - tCurrentPosX.get()) == 1 &&
                            Math.abs(hCurrentPosY.get() - tCurrentPosY.get()) == 2) {
                        if (hCurrentPosX.get() > tCurrentPosX.get() || hCurrentPosY.get() > tCurrentPosY.get()) {
                            newX++;
                            newY++;
                        } else {
                            newX--;
                            newY--;
                        }
                    }
                }
            }

           if (tMatrix.size() <= newX){
               tMatrix.removeAll(tMatrix);
               tMatrix.addAll((List) Arrays.asList(new ArrayList[newX + 1]));
           }
           tMatrix.set(newX, Arrays.asList(new String[newY + 1]));
           tMatrix.get(newX).set(newY, "T"); //put T in the new location
           tCurrentPosX.set(newX);
           tCurrentPosY.set(newY);
        }

        tPositionHistory.add(tCurrentPosX.get() + " " + tCurrentPosY.get());
    }
}
