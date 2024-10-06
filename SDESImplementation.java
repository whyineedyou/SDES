public class SDESImplementation {
    // 密钥扩展
    public static int keyExpansion(int key) {
        int[] PC1 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        int left = key >> 5 & 0xFF;
        int right = key & 0xFF;

        int leftPc1 = permute(left, PC1);
        int rightPc1 = permute(right, PC1);

        int[] PC2 = {6, 3, 7, 4, 8, 5, 10, 9};
        int leftPc2 = permute(leftPc1, PC2);
        int rightPc2 = permute(rightPc1, PC2);

        return (leftPc2 << 6) | (rightPc2 & 0x3F);
    }

    // 初始置换
    public static int initialPermutation(int data) {
        int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
        return permute(data, IP);
    }

    // 最终置换
    public static int finalPermutation(int data) {
        int[] IPInverse = {4, 1, 3, 5, 7, 2, 8, 6};
        return permute(data, IPInverse);
    }

    // 轮函数
    public static int roundFunction(int right, int subKey) {
        int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
        int extended = permute(right, EP);
        int xor = extended ^ subKey;
        int s1 = sBox(0, xor);
        int s2 = sBox(1, xor);

        int newLeft = (s1 << 4) | s2;
        int newRight = (s2 << 4) | s1;

        int[] P = {2, 4, 3, 1};
        int permuted = permute(newLeft, P);
        return permuted ^ newRight;
    }

    // S盒
    public static int sBox(int sBoxNum, int input) {
        int[][] S1 = {
                {1, 0, 3, 2},
                {3, 2, 1, 0},
                {0, 2, 1, 3},
                {3, 1, 0, 2}
        };

        int[][] S2 = {
                {0, 1, 2, 3},
                {2, 3, 1, 0},
                {3, 0, 1, 2},
                {2, 1, 0, 3}
        };

        int row = (input >> 4) & 0b11;
        int col = input & 0b11;

        return (S1[sBoxNum][row] << 2) | S2[sBoxNum][col];
    }

    // 置换函数
    private static int permute(int data, int[] permutation) {
        int result = 0;
        for (int i = 0; i < permutation.length; i++) {
            if ((data & (1 << permutation[i] - 1)) != 0) {
                result |= 1 << i;
            }
        }
        return result;
    }

    // 加密函数
    public static int encrypt(int data, int key) {
        int permutedData = initialPermutation(data);
        int left = permutedData >> 4 & 0xF;
        int right = permutedData & 0xF;

        int subKey = keyExpansion(key);
        int round1 = roundFunction(right, subKey);
        left = left ^ round1;

        int temp = left;
        left = right;
        right = temp;

        int round2 = roundFunction(right, subKey);
        left = left ^ round2;

        int finalData = (left << 4) | right;
        return finalPermutation(finalData);
    }

    public static int decrypt(int data, int key) {
        int permutedData = finalPermutation(data);
        int left = permutedData >> 4 & 0xF;
        int right = permutedData & 0xF;

        int subKey = keyExpansion(key);
        int round1 = roundFunction(left, subKey);
        right = right ^ round1;

        int temp = left;
        left = right;
        right = temp;

        int round2 = roundFunction(left, subKey);
        left = left ^ round2;

        int finalData = (left << 4) | right;
        return initialPermutation(finalData);
    }

    public static String asciiToBinary(String asciiStr) {
        StringBuilder binaryStr = new StringBuilder();
        for (char c : asciiStr.toCharArray()) {
            int data = (int) c;
            String binary = Integer.toBinaryString(data);
            binary = String.format("%8s", binary).replace(' ', '0');
            binaryStr.append(binary);
        }
        return binaryStr.toString();
    }
}