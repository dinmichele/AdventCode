package my;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RopeBridge2 {


    public static void main(String[] args) throws IOException {
        // Example input

        List<String> input = Files.readAllLines(Paths.get("resources/source_day9.txt"));

        Set<Position> positions = new HashSet<>();  // set to store visited positions

        // initialize current position of head and tail
        int headX = 0;
        int headY = 0;
        int tailX = 0;
        int tailY = 0;

        // initialize direction the head is facing
        String direction = "R";

        // Parse input
        for (String l : input) {
            String[] command = l.split(" ");
            // parse movement command
            String action = command[0];
            int steps = Integer.parseInt(command[1]);

            // update direction the head is facing
            if (action.equals("R")) {
                direction = "R";
            } else if (action.equals("L")) {
                direction = "L";
            } else if (action.equals("U")) {
                direction = "U";
            } else if (action.equals("D")) {
                direction = "D";
            }

            // update position of head
            if (direction.equals("R")) {
                headX += steps;
            } else if (direction.equals("L")) {
                headX -= steps;
            } else if (direction.equals("U")) {
                headY += steps;
            } else if (direction.equals("D")) {
                headY -= steps;
            }

            // if head and tail are not touching, move tail one step diagonally towards head
            if (Math.abs(headX - tailX) > 1 || Math.abs(headY - tailY) > 1) {
                if (headX > tailX) {
                    tailX += 1;
                } else if (headX < tailX) {
                    tailX -= 1;
                }
                if (headY > tailY) {
                    tailY += 1;
                } else if (headY < tailY) {
                    tailY -= 1;
                }
            }
            // if head and tail are touching, but head is not two steps away from tail in any direction, do nothing
            else if (Math.abs(headX - tailX) == 1 && Math.abs(headY - tailY) == 1) {
                // do nothing
            }
            // if head is two steps away from tail in any direction, move tail one step in that direction
            else {
                if (headX > tailX) {
                    tailX += 1;
                } else if (headX < tailX) {
                    tailX -= 1;
                }
                if (headY > tailY) {
                    tailY += 1;
                } else if (headY < tailY) {
                    tailY -= 1;
                }
            }

            // add current position of tail to set of visited positions
            positions.add(new Position(tailX, tailY));
        }


        //print number of unique positions visited by tail
        System.out.println("N. of visited position: " + positions.size());
    }

    private static class Position {
        int x;
        int y;
        public Position(int tailX, int tailY) {
            x = tailX;
            y = tailY;
        }
    }
}
