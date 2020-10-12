package SecretSharing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hussain
 */


public class SecretSharingAlgorithm {
    private int num;  //number of user
    private int particNum;   // number of participants
    private int threshold; 
    

    public SecretSharingAlgorithm(int num, int particNum, int threshold) {
        this.num = num;
        this.particNum = particNum;
        this.threshold = threshold;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getParticNum() {
        return particNum;
    }

    public void setParticNum(int particNum) {
        this.particNum = particNum;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
   
    
    
    
    
}
