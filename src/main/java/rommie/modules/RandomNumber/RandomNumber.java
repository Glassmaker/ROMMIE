package rommie.modules.RandomNumber;

import rommie.Rommie;

import java.util.Random;

public class RandomNumber {

    public static int generateRandom() {
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 10; ++idx) {
            int randomInt = randomGenerator.nextInt(Rommie.Fox.length);
            return randomInt;
        }
        return 3;
    }
}
