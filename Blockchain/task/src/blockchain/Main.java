package blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class block {
    public int id;
    private long timestamp;
    private String previousHashBlock;
    private String hashOfBlock;

    public block(int id, long timestamp, String previousHashBlock, String hashOfBlock) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousHashBlock = previousHashBlock;
        this.hashOfBlock = hashOfBlock;

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
}



public class Main {

    public static void main(String args[]){
        List<block> blockChain = new ArrayList<block>();
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
        blockChain.add(createBlock(blockChain));
       for(block b:blockChain){
           System.out.println("Block:");
           System.out.println("Id: " + b.getId());
           System.out.println("Timestamp: " + b.getTimestamp());
           System.out.println("Hash of the previous block:");
           System.out.println(b.getPreviousHashBlock());
           System.out.println("Hash of the block:");
           System.out.println(b.getHashOfBlock());
           System.out.println();
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
        if(blockChain.size()==0){
            int id = 1;
            String previousHashBlock = "0";
            long timestamp = new Date().getTime();
            String sha256input = String.valueOf(id + previousHashBlock+ timestamp);
            String hashOfBlock = applySha256(sha256input);

            return new block(id,timestamp,previousHashBlock,hashOfBlock);
        }
        else {
            int id = blockChain.size() + 1;
            String previousHashBlock = blockChain.get(blockChain.size() - 1).getHashOfBlock();
            long timestamp = new Date().getTime();
            String sha26input = String.valueOf(id + previousHashBlock+ timestamp);
            String hashOfBlock = applySha256(sha26input);

            return new block(id,timestamp,previousHashBlock,hashOfBlock);
        }
    }

}
