package rommie.modules.RandomNumber;

import rommie.Rommie;

import java.util.Random;

public class RandomNumber {

    public static int generateRandom(int max) {
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 10; ++idx) {
            int randomInt = randomGenerator.nextInt(max);
            return randomInt;
        }
        return 3;
    }
}
