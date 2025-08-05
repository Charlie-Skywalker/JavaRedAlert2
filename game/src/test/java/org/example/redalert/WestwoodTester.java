package org.example.redalert;

import java.util.Arrays;

public class WestwoodTester {
    private static final String publicKeyStr = "AihRvNoIbTn85FZRYNZRcT+i6KpU+maCsEqr3Q5q+LDB5tH7Tz2qQ38V";
    
    private static final byte[] char2num = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
        -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };
    
    // 大数结构 (64个int = 2048位)
    private static int[] key1 = new int[64];
    private static int[] key2 = new int[64];
    private static int pubkeyLen;
    
    // 全局计算变量
    private static int[] glob1 = new int[64];
    private static int glob1_bitlen, glob1_len_x2;
    private static int[] glob2 = new int[130];
    private static int[] glob1_hi = new int[4];
    private static int[] glob1_hi_inv = new int[4];
    private static int glob1_hi_bitlen;
    private static int glob1_hi_inv_lo, glob1_hi_inv_hi;
    
    private static boolean publicKeyInitialized = false;
    
    // 初始化大数为指定值
    private static void initBigNum(int[] n, int val, int len) {
        Arrays.fill(n, 0, len, 0);
        n[0] = val;
    }
    
    // 移动密钥到大数
    private static void moveKeyToBigNum(int[] n, byte[] key, int klen, int blen) {
        int sign = (key[0] & 0x80) != 0 ? 0xFF : 0;
        int byteIndex = blen * 4 - 1;
        
        // 填充符号位
        for (; byteIndex >= klen; byteIndex--) {
            n[byteIndex / 4] |= sign << (8 * (byteIndex % 4));
        }
        
        // 复制密钥数据
        for (int i = 0; i < klen && byteIndex >= 0; byteIndex--, i++) {
            int shift = 8 * (byteIndex % 4);
            n[byteIndex / 4] = (n[byteIndex / 4] & ~(0xFF << shift)) |
                ((key[klen - 1 - i] & 0xFF) << shift);
        }
    }
    
    // 密钥转大数
    // 获取大数的有效长度
    private static void convertKeyToBigNum(int[] n, byte[] key, int len) {
        if (key[0] != 2) return;
        int keyLen;
        int index = 1;
        
        if ((key[1] & 0x80) != 0) {
            int count = key[1] & 0x7F;
            keyLen = 0;
            for (int i = 0; i < count; i++) {
                keyLen = (keyLen << 8) | (key[++index] & 0xFF);
            }
            index++;
        } else {
            keyLen = key[index++] & 0xFF;
        }
        
        if (keyLen <= len * 4) {
            byte[] actualKey = Arrays.copyOfRange(key, index, index + keyLen);
            moveKeyToBigNum(n, actualKey, keyLen, len);
        }
    }
    private static int getBigNumLength(int[] n, int len) {
        int i = len - 1;
        while (i >= 0 && n[i] == 0) i--;
        return i + 1;
    }
    
    // 计算大数的位长度
    private static int getBigNumBitLength(int[] n, int len) {
        int ddlen = getBigNumLength(n, len);
        if (ddlen == 0) return 0;
        
        int bitlen = ddlen * 32;
        int mask = 0x80000000;
        while ((n[ddlen - 1] & mask) == 0) {
            mask >>>= 1;
            bitlen--;
        }
        return bitlen;
    }
    
    // 初始化公钥
    private static void initPublicKey() {
        byte[] keytmp = new byte[256];
        int i = 0, i2 = 0;
        
        // Base64解码
        while (i < publicKeyStr.length()) {
            int tmp = char2num[publicKeyStr.charAt(i++)] & 0xFF;
            tmp = (tmp << 6) | (char2num[publicKeyStr.charAt(i++)] & 0xFF);
            tmp = (tmp << 6) | (char2num[publicKeyStr.charAt(i++)] & 0xFF);
            tmp = (tmp << 6) | (char2num[publicKeyStr.charAt(i++)] & 0xFF);
            
            keytmp[i2++] = (byte) ((tmp >> 16) & 0xFF);
            keytmp[i2++] = (byte) ((tmp >> 8) & 0xFF);
            keytmp[i2++] = (byte) (tmp & 0xFF);
        }
        
        // 设置公钥参数
        initBigNum(key2, 0x10001, 64);
        convertKeyToBigNum(key1, keytmp, 64);
        pubkeyLen = getBigNumBitLength(key1, 64) - 1;
    }
    
    // 计算预处理数据长度
    private static int getPreDataLength() {
        int a = (pubkeyLen - 1) / 8;
        return (55 / a + 1) * (a + 1);
    }
    
    // 大数比较
    private static int compareBigNum(int[] n1, int[] n2, int len) {
        for (int i = len - 1; i >= 0; i--) {
            long val1 = n1[i] & 0xFFFFFFFFL;
            long val2 = n2[i] & 0xFFFFFFFFL;
            if (val1 < val2) return -1;
            if (val1 > val2) return 1;
        }
        return 0;
    }
    
    // 大数移位（右移）
    private static void shiftRightBigNum(int[] n, int bits, int len) {
        int wordShift = bits / 32;
        bits %= 32;
        
        if (wordShift > 0) {
            System.arraycopy(n, wordShift, n, 0, len - wordShift);
            Arrays.fill(n, len - wordShift, len, 0);
        }
        
        if (bits != 0) {
            for (int i = 0; i < len - 1; i++) {
                n[i] = (n[i] >>> bits) | (n[i + 1] << (32 - bits));
            }
            n[len - 1] >>>= bits;
        }
    }
    
    // 大数移位（左移）
    private static void shiftLeftBigNum(int[] n, int bits, int len) {
        int wordShift = bits / 32;
        bits %= 32;
        
        if (wordShift > 0) {
            for (int i = len - 1; i >= wordShift; i--) {
                n[i] = n[i - wordShift];
            }
            Arrays.fill(n, 0, wordShift, 0);
        }
        
        if (bits != 0) {
            for (int i = len - 1; i > 0; i--) {
                n[i] = (n[i] << bits) | (n[i - 1] >>> (32 - bits));
            }
            n[0] <<= bits;
        }
    }
    
    // 大数减法
    private static int subBigNum(int[] dest, int[] src1, int[] src2, int carry, int len) {
        long borrow = carry & 0xFFFFFFFFL;
        for (int i = 0; i < len; i++) {
            long val1 = src1[i] & 0xFFFFFFFFL;
            long val2 = src2[i] & 0xFFFFFFFFL;
            long result = val1 - val2 - borrow;
            
            dest[i] = (int) result;
            borrow = result < 0 ? 1 : 0;
        }
        return (int) borrow;
    }
    
    // 大数取模逆元
    private static void inv_bignum(int[] n1, int[] n2, int len) {
        int[] n_tmp = new int[len];
        initBigNum(n_tmp, 0, len);
        initBigNum(n1, 0, len);
        
        int n2_bitlen = getBigNumBitLength(n2, len);
        int bit = 1 << (n2_bitlen % 32);
        int pos = (n2_bitlen + 31) / 32 - 1;
        
        Arrays.fill(n_tmp, 0);
        n_tmp[n2_bitlen / 32] = 1 << (n2_bitlen % 32);
        
        while (n2_bitlen > 0) {
            n2_bitlen--;
            shiftLeftBigNum(n_tmp, 1, len);
            
            if (compareBigNum(n_tmp, n2, len) >= 0) {
                subBigNum(n_tmp, n_tmp, n2, 0, len);
                n1[pos] |= bit;
            }
            
            bit >>>= 1;
            if (bit == 0) {
                bit = 0x80000000;
                pos--;
            }
        }
    }
    
    // 大数自增
    private static void inc_bignum(int[] n, int len) {
        for (int i = 0; i < len; i++) {
            if (++n[i] != 0) break;
        }
    }
    
    // 初始化全局计算参数
    private static void init_two_dw(int[] n, int len) {
        System.arraycopy(n, 0, glob1, 0, len);
        glob1_bitlen = getBigNumBitLength(glob1, len);
        glob1_len_x2 = (glob1_bitlen + 15) / 16;
        
        int n_len = getBigNumLength(glob1, len);
        System.arraycopy(glob1, n_len - 2, glob1_hi, 0, 2);
        glob1_hi_bitlen = getBigNumBitLength(glob1_hi, 2) - 32;
        shiftRightBigNum(glob1_hi, glob1_hi_bitlen, 2);
        
        inv_bignum(glob1_hi_inv, glob1_hi, 2);
        shiftRightBigNum(glob1_hi_inv, 1, 2);
        glob1_hi_bitlen = (glob1_hi_bitlen + 15) % 16 + 1;
        inc_bignum(glob1_hi_inv, 2);
        
        if (getBigNumBitLength(glob1_hi_inv, 2) > 32) {
            shiftRightBigNum(glob1_hi_inv, 1, 2);
            glob1_hi_bitlen--;
        }
        
        glob1_hi_inv_lo = glob1_hi_inv[0] & 0xFFFF;
        glob1_hi_inv_hi = (glob1_hi_inv[0] >>> 16) & 0xFFFF;
    }
    
    // 大数字节移位（右移）
    private static void move_bytes_right(int[] n, int bytes) {
        int len = n.length;
        for (int i = 0; i < len - 1; i++) {
            n[i] = (n[i] >>> (bytes * 8)) | (n[i + 1] << (32 - bytes * 8));
        }
        n[len - 1] >>>= (bytes * 8);
    }
    
    // 大数字节移位（左移）
    private static void move_bytes_left(int[] n, int bytes) {
        int len = n.length;
        for (int i = len - 1; i > 0; i--) {
            n[i] = (n[i] << (bytes * 8)) | (n[i - 1] >>> (32 - bytes * 8));
        }
        n[0] <<= (bytes * 8);
    }
    
    // 大数乘法（单字）
    private static void mul_bignum_word(int[] n1, int[] n2, int mul, int len) {
        long carry = 0;
        for (int i = 0; i < len; i++) {
            long product = (mul & 0xFFFFFFFFL) * (n2[i] & 0xFFFFFFFFL) +
                (n1[i] & 0xFFFFFFFFL) + carry;
            n1[i] = (int) product;
            carry = product >>> 32;
        }
        if (len < n1.length) {
            n1[len] = (int) carry;
        }
    }
    
    // 大数乘法
    private static void mul_bignum(int[] dest, int[] src1, int[] src2, int len) {
        int fullLen = len * 2;
        initBigNum(dest, 0, fullLen);
        int[] temp = new int[fullLen];
        
        for (int i = 0; i < len; i++) {
            System.arraycopy(dest, 0, temp, 0, fullLen);
            mul_bignum_word(temp, src1, src2[i], len);
            
            if (i > 0) {
                for (int j = fullLen - 1; j >= i; j--) {
                    temp[j] = j >= i ? temp[j - i] : 0;
                }
                Arrays.fill(temp, 0, i, 0);
            }
            
            int carry = 0;
            for (int j = 0; j < fullLen; j++) {
                long sum = (dest[j] & 0xFFFFFFFFL) +
                    (temp[j] & 0xFFFFFFFFL) + carry;
                dest[j] = (int) sum;
                carry = (int) (sum >>> 32);
            }
        }
    }
    
    // 大数取反
    private static void not_bignum(int[] n, int len) {
        for (int i = 0; i < len; i++) {
            n[i] = ~n[i];
        }
    }
    
    // 大数取负
    private static void neg_bignum(int[] n, int len) {
        not_bignum(n, len);
        inc_bignum(n, len);
    }
    
    // 大数自减
    private static void dec_bignum(int[] n, int len) {
        for (int i = 0; i < len; i++) {
            if (--n[i] != -1) break;
        }
    }
    
    // 模约减计算
    private static void calc_a_bignum(int[] n1, int[] n2, int[] n3, int len) {
        int carry;
        int[] n_tmp = new int[len];
        
        // glob2 = n2 * n3
        mul_bignum(glob2, n2, n3, len);
        
        // 处理高位进位
        if (glob2.length > len * 2) glob2[len * 2] = 0;
        
        int g2_len_x2 = getBigNumLength(glob2, len * 2 + 1) * 2;
        if (g2_len_x2 >= glob1_len_x2) {
            inc_bignum(glob2, len * 2 + 1);
            neg_bignum(glob2, len * 2 + 1);
            
            int len_diff = g2_len_x2 + 1 - glob1_len_x2;
            int[] temp_glob2 = Arrays.copyOf(glob2, glob2.length);
            
            // 优化计算步骤
            for (int i = 0; i < len_diff; i++) {
                int low_word = glob2[i] & 0xFFFF;
                int high_word = (glob2[i] >>> 16) & 0xFFFF;
                
                // 简化计算核心
                int tmp = (((((low_word ^ 0xFFFF) * glob1_hi_inv_lo + 0x10000) >>> 1)
                    + (((high_word ^ 0xFFFF) * glob1_hi_inv_hi + glob1_hi_inv_hi) >>> 1) + 1)
                    >>> 16) + glob1_hi_inv_hi * (low_word ^ 0xFFFF) * 2;
                
                tmp = (tmp >>> glob1_hi_bitlen) & 0xFFFF;
                if (tmp > 0) {
                    // 优化乘法步骤
                    int[] temp = new int[len];
                    System.arraycopy(glob1, 0, temp, 0, len);
                    for (int j = 0; j < len; j++) {
                        temp[j] *= tmp;
                    }
                    
                    carry = subBigNum(glob2, glob2, temp, 0, len);
                    if (carry != 0) {
                        glob2[i]--;
                    }
                }
            }
            neg_bignum(glob2, len);
            dec_bignum(glob2, len);
        }
        System.arraycopy(glob2, 0, n1, 0, len);
    }
    
    // 核心模幂运算
    private static void calc_a_key(int[] n1, int[] n2, int[] n3, int[] n4, int len) {
        int[] n_tmp = new int[len];
        initBigNum(n1, 1, len);
        
        int n4_len = getBigNumLength(n4, len);
        init_two_dw(n4, n4_len);
        
        int n3_bitlen = getBigNumBitLength(n3, n4_len);
        int n3_len = (n3_bitlen + 31) / 32;
        int bit_mask = 0x80000000 >>> (n3_bitlen % 32);
        int index = n3_len - 1;
        System.arraycopy(n2, 0, n1, 0, n4_len);
        
        while (--n3_bitlen >= 0) {
            if (bit_mask == 0) {
                bit_mask = 0x80000000;
                index--;
            }
            
            calc_a_bignum(n_tmp, n1, n1, n4_len);
            if ((n3[index] & bit_mask) != 0) {
                calc_a_bignum(n1, n_tmp, n2, n4_len);
            } else {
                System.arraycopy(n_tmp, 0, n1, 0, n4_len);
            }
            bit_mask >>>= 1;
        }
    }
    
    // 处理预处理数据
    private static void processPreData(byte[] pre, int pre_len, byte[] buf) {
        int a = (pubkeyLen - 1) / 8;
        int buf_index = 0;
        
        while (a + 1 <= pre_len) {
            int[] n2 = new int[64];
            // 将输入数据复制到大数
            for (int i = 0; i < a + 1; i++) {
                int bytePos = i;
                int shift = (bytePos % 4) * 8;
                if (shift == 0) n2[bytePos / 4] = 0;
                n2[bytePos / 4] |= (pre[i] & 0xFF) << shift;
            }
            
            int[] n3 = new int[64];
            // 模幂计算: n3 = n2^key2 mod key1
            calc_a_key(n3, n2, key2, key1, 64);
            
            // 结果复制到输出
            for (int i = 0; i < a; i++) {
                int shift = (i % 4) * 8;
                buf[buf_index++] = (byte) (n3[i / 4] >>> shift);
            }
            
            pre_len -= a + 1;
            pre = Arrays.copyOfRange(pre, a + 1, pre.length + a + 1);
        }
    }
    
    // 主函数：生成Blowfish密钥
    public static byte[] getBlowfishKey(byte[] s) {
        if (!publicKeyInitialized) {
            initPublicKey();
            publicKeyInitialized = true;
        }
        
        int preDataLength = getPreDataLength();
        if (s.length < preDataLength) {
            throw new IllegalArgumentException("Input data too short");
        }
        
        byte[] key = new byte[256];
        processPreData(s, preDataLength, key);
        return Arrays.copyOf(key, 56);
    }
    
    public static void main(String[] args) {
        // 示例测试数据
        byte[] input = new byte[100];
        Arrays.fill(input, (byte) 0xAB);
        
        byte[] blowfishKey = getBlowfishKey(input);
        System.out.println("Generated Blowfish Key: ");
        for (byte b : blowfishKey) {
            System.out.printf("%02X ", b);
        }
    }
}
