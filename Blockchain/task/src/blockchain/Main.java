package blockchain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

class block implements Serializable {
    public int id;
    public final int minerId;
    private final long timestamp;
    private final String previousHashBlock;
    private final String hashOfBlock;
    private final int timeRequiredToGenerateBlock;
    public int numberOfZeros;
    private final int magicNumber;

    public block(int id, long timestamp, String previousHashBlock, String hashOfBlock,int timeRequiredToGenerateBlock,int magicNumber,int minerId,int numberOfZeros) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousHashBlock = previousHashBlock;
        this.hashOfBlock = hashOfBlock;
        this.timeRequiredToGenerateBlock = timeRequiredToGenerateBlock;
        this.magicNumber = magicNumber;
        this.minerId = minerId;
        this.numberOfZeros = numberOfZeros;
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

    public int getTimeRequiredToGenerateBlock() {
        return timeRequiredToGenerateBlock;
    }

    public int getId() {
        return id;
    }
    public void PrintBlock(){
        System.out.println("Block:");
        System.out.println("Created by miner # " + minerId);
        System.out.println("Id: " + id);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Magic number: "+ magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(previousHashBlock);
        System.out.println("Hash of the block:");
        System.out.println(hashOfBlock);
        System.out.println("Block was generating for " + timeRequiredToGenerateBlock + " seconds");

    }
}

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        List<block> blockChain = new ArrayList<>();
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public static block createBlock(List<block> blockChain){
        long timestamp = new Date().getTime();
        int poolSize = Runtime.getRuntime().availableProcessors();

        String zerosString = "";

        String hashOfBlock ="qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";

        if(blockChain.size()==0){

            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            int id = 1;
            String previousHashBlock = "0";
            String finalZerosString = zerosString;
            block[] tempChain = new block[poolSize];
            int[] timeReqToCreateBlockList = new int[poolSize];
            for(int i = 0;i < poolSize;i++) {
                int finalI = i;
                int finalNumberOfZeros = 0;
                executor.submit(() -> {
                    try {
                        //System.out.println(Thread.currentThread().getName().substring(14, 15)+"index");
                        block bl = getBlock(finalNumberOfZeros, timestamp, finalZerosString, hashOfBlock, id, previousHashBlock, Integer.parseInt(Thread.currentThread().getName().substring(14, 15)));
                        int time = bl.getTimeRequiredToGenerateBlock();
                        // System.out.println("time :"+ time);
                        tempChain[finalI] = bl;
                        timeReqToCreateBlockList[finalI] = time;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    });
            }
            awaitTerminationAfterShutdown(executor);
          // System.out.println(executor.awaitTermination(60,TimeUnit.MILLISECONDS););
            int min = timeReqToCreateBlockList[0];
            int index=0;
           // System.out.println(timeReqToCreateBlockList.length + "size" + tempChain.length);
            for(int i = 0; i < timeReqToCreateBlockList.length; i++) {
              //  System.out.println(timeReqToCreateBlockList[i] + "time");
                if(min > timeReqToCreateBlockList[i])
                {
                    min = timeReqToCreateBlockList[i];
                    index=i;
                }
            }
            tempChain[index].PrintBlock();
            tempChain[index].numberOfZeros += 1;
            System.out.println("N was increased to "+tempChain[index].numberOfZeros);
            System.out.println();
            return tempChain[index];//getBlock(numberOfZeros, timestamp, zerosString, hashOfBlock, id, previousHashBlock,minerId);
        }
        else {
          int numberOfZeros = blockChain.get(blockChain.size()-1).numberOfZeros;
            for (int i = 0;i<numberOfZeros;i++){
                zerosString = "0" + zerosString;
            }
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            int id = blockChain.size() + 1;
            String previousHashBlock = blockChain.get(blockChain.size() - 1).getHashOfBlock();
            String finalZerosString = zerosString;
            block[] tempChain = new block[poolSize];
            int[] timeReqToCreateBlockList = new int[poolSize];
            for(int i = 0;i < poolSize;i++) {
                int finalI = i;
                int finalNumberOfZeros1 = numberOfZeros;
                executor.submit(() -> {
                    //System.out.println(Thread.currentThread().getName().substring(14, 15)+"index");
                    block bl =  getBlock(finalNumberOfZeros1, timestamp, finalZerosString, hashOfBlock, id, previousHashBlock, Integer.parseInt(Thread.currentThread().getName().substring(14, 15)));
                    int time = bl.getTimeRequiredToGenerateBlock();
                   // System.out.println("time :"+ time);
                    tempChain[finalI] = bl;
                    timeReqToCreateBlockList[finalI] = time;
                });
            }
            awaitTerminationAfterShutdown(executor);
            // System.out.println(executor.awaitTermination(60,TimeUnit.MILLISECONDS););
            int min = timeReqToCreateBlockList[0];
            int index=0;
           // System.out.println(timeReqToCreateBlockList.length + "size" + tempChain.length);
            for(int i = 0; i < timeReqToCreateBlockList.length; i++) {
               // System.out.println(timeReqToCreateBlockList[i] + "time");
                if(min > timeReqToCreateBlockList[i])
                {
                    min = timeReqToCreateBlockList[i];
                    index=i;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(tempChain[index]!=null) {
                tempChain[index].PrintBlock();
                if (tempChain[index].numberOfZeros < 2) {
                    tempChain[index].numberOfZeros += 1;
                    System.out.println("N was increased to " + tempChain[index].numberOfZeros);
                    System.out.println();
                } else if (tempChain[index].numberOfZeros == 2) {
                    System.out.println("N stays the same");
                    System.out.println();
                } else {
                    tempChain[index].numberOfZeros -= 1;
                    System.out.println("N was decreased by 1");
                    System.out.println();
                }
            }
            else {
                block bl = getBlock(numberOfZeros, timestamp, finalZerosString, hashOfBlock, id, previousHashBlock, 1);
                bl.PrintBlock();
                System.out.println("N stays the same");
                System.out.println();
                tempChain[index] = bl;
            }
            return tempChain[index];//getBlock(numberOfZeros, timestamp, zerosString, hashOfBlock, id, previousHashBlock,minerId);
        }
    }

    @NotNull
    private static block getBlock(int numberOfZeros, long timestamp, String zerosString, String hashOfBlock, int id, String previousHashBlock,int minerId) {
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

        return new block(id,timestamp,previousHashBlock,hashOfBlock,(int)((new Date().getTime()/1000) - (timestamp/1000)),magicNumber,minerId,numberOfZeros);
    }

    public static boolean validate(List<block> blockChain,int numberOfZeros){
        boolean result = true;
        String zerosString = "";
        for (int i = 0;i<numberOfZeros;i++){
            zerosString = "0" + zerosString;
        }
        if(blockChain.size()==0){
            return true;
        }
        if(blockChain.size()==1){
            return blockChain.get(0).getPreviousHashBlock().equals("0") && zerosString.equals(blockChain.get(0).getHashOfBlock().substring(0, numberOfZeros));
        }
        else {
            for(int i = 0;i < blockChain.size();i++){
                if(i==0){
                    result = blockChain.get(0).getPreviousHashBlock().equals("0") && zerosString.equals(blockChain.get(0).getHashOfBlock().substring(0, numberOfZeros));
                }
                else {
                    result = blockChain.get(i).getPreviousHashBlock().equals(blockChain.get(i-1).getHashOfBlock()) && zerosString.equals(blockChain.get(i).getHashOfBlock().substring(0, numberOfZeros));
                }
            }
        }
        return result;
    }
    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(600, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}

