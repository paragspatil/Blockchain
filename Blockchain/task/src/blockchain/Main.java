package blockchain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class block implements Serializable {
    public int id;
    private final long timestamp;
    private final String previousHashBlock;
    private final String hashOfBlock;
    private final int timeRequiredToGenerateBlock;
    public static int numberOfZeros;
    private final int magicNumber;

    public block(int id, long timestamp, String previousHashBlock, String hashOfBlock,int timeRequiredToGenerateBlock,int magicNumber) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousHashBlock = previousHashBlock;
        this.hashOfBlock = hashOfBlock;
        this.timeRequiredToGenerateBlock = timeRequiredToGenerateBlock;
        this.magicNumber = magicNumber;
    }

    public static void setNumberOfZeros(int numberOfZeros) {
        block.numberOfZeros = numberOfZeros;
    }

    public String getPreviousHashBlock() {
        return previousHashBlock;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHashOfBlock() {
        return hashOfBlock;
    }

    public int getId() {
        return id;
    }
    public void PrintBlock(){
        System.out.println("Block:");
        System.out.println("Id: " + id);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Magic number: "+ magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(previousHashBlock);
        System.out.println("Hash of the block:");
        System.out.println(hashOfBlock);
        System.out.println("Block was generating for " + timeRequiredToGenerateBlock + " seconds");
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int numberOfZeros = scanner.nextInt();
        List<block> blockChain = new ArrayList<>();
        blockChain.add(createBlock(blockChain,numberOfZeros));
        blockChain.add(createBlock(blockChain,numberOfZeros));
        blockChain.add(createBlock(blockChain,numberOfZeros));
        blockChain.add(createBlock(blockChain,numberOfZeros));
        blockChain.add(createBlock(blockChain,numberOfZeros));
        for(block b:blockChain){
            b.PrintBlock();
        }
    }

    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static block createBlock(List<block> blockChain,int numberOfZeros){
        long timestamp = new Date().getTime();
        String zerosString = "";
        for (int i = 0;i<numberOfZeros;i++){
            zerosString = "0" + zerosString;
        }
        String hashOfBlock ="qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";

        if(blockChain.size()==0){
            int id = 1;
            String previousHashBlock = "0";
            return getBlock(numberOfZeros, timestamp, zerosString, hashOfBlock, id, previousHashBlock);
        }
        else {
            int id = blockChain.size() + 1;
            String previousHashBlock = blockChain.get(blockChain.size() - 1).getHashOfBlock();
            return getBlock(numberOfZeros, timestamp, zerosString, hashOfBlock, id, previousHashBlock);
        }
    }

    @NotNull
    private static block getBlock(int numberOfZeros, long timestamp, String zerosString, String hashOfBlock, int id, String previousHashBlock) {
        int magicNumber = 0;
        if(numberOfZeros>0) {
            while (!zerosString.equals(hashOfBlock.substring(0, numberOfZeros))) {
                magicNumber = ThreadLocalRandom.current().nextInt();
                String sha256input = id + previousHashBlock + timestamp + magicNumber;
                hashOfBlock = applySha256(sha256input);
            }
        }
        else {
            String sha256input = id + previousHashBlock + timestamp;
            hashOfBlock = applySha256(sha256input);
        }

        return new block(id,timestamp,previousHashBlock,hashOfBlock,(int)((new Date().getTime()/1000) - (timestamp/1000)),magicNumber);
    }

}

