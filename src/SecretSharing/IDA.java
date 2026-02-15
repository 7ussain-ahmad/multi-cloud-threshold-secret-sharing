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
import java.util.Arrays;

public class IDA {

    private static final int FIELD_SIZE = 256;				//field size

    public static int n, m;

    public static int[][] encodedMatrix;
    public final int[][] mulsInGF256 = new int[FIELD_SIZE][FIELD_SIZE];
    public static int[][] vandermonde;
    public static int[][] inverseVandermonde;
    public static int[] inverse = new int[FIELD_SIZE];
    public static int[] integerSeq;

    public IDA(int n, int m) {
        this.n = n;
        this.m = m;

        //Compute multiplication table
        //حساب جدول الضرب
        for (int a = 0; a < 256; a++) {
            for (int b = 0; b < 256; b++) {
                mulsInGF256[a][b] = mulInGF256(a, b);
            }
        }
        //creates the vandermonde table
        vandermonde = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                vandermonde[i][j] = (j == 0 ? 1 : mulsInGF256[vandermonde[i][j - 1]][i]);
            }
        }
        //inverse the vandermonde table
        inverseVandermonde = new int[m][m];
        for (int a = 1; a < 256; a++) {
            if (inverse[a] == 0) {
                for (int b = 1; b < 256; b++) {
                    if (mulsInGF256[a][b] == 1) {
                        inverse[a] = b;
                        inverse[b] = a;
                    }
                }
            }
        }
    }

    /**
     * encodes the String with vandermonde matrix
     *
     * @param sequence
     * @return
     */
    public int[][] encode(int[] sequence) {
        int[] encodedData;
        //  String []str = null;
        //convert to integer under GF(256)		
        if (sequence.length % m != 0) {
            integerSeq = new int[sequence.length + m - sequence.length % m];
            for (int x = 0; x < integerSeq.length; x++) {
//				if (x==0)
//					integerSeq[x]=sequence.length;
//				else if(x<sequence.length+1 && x>0)
//					integerSeq[x]=sequence[x-1];
                if (x < sequence.length) {
                    integerSeq[x] = (sequence[x]);
                } else {
                    integerSeq[x] = 0;
                }
            }
        } else {
            integerSeq = new int[sequence.length];
            integerSeq = sequence;
//			for (int x=0; x<integerSeq.length; x++){
//				
//				 if(x<sequence.length && x>0)
//					integerSeq[x]=sequence[x];
//				else 
//					integerSeq[x]=0;
//			}
        }

        encodedMatrix = new int[n][integerSeq.length / m];

        int index = 0;

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < integerSeq.length / m; i++) {
                for (int j = 0; j < m; j++) {
                    encodedMatrix[k][i] = addInGF256(encodedMatrix[k][i],
                            mulsInGF256[vandermonde[k][j]][integerSeq[index]]);
                    index++;
                }
            }
            //   str[k]=Arrays.toString(encodedMatrix[k]);
            index = 0;
        }
//		encodedData= new int[encodedMatrix.length*encodedMatrix[0].length];
//		
//		//set to data buffer
//		for (int x=0; x<encodedMatrix.length; x++)
//			for (int y=0;y<encodedMatrix[0].length; y++)
//				encodedData[x*encodedMatrix[0].length+y]=encodedMatrix[x][y];
//		
        return encodedMatrix;
    }

    /**
     *
     * @param encodedMatrix
     * @param labels
     * @return
     */
    public int[] decode(int[][] encodedSeq, int[] labels) {
        int[] decodedSeq = new int[encodedMatrix[0].length * m];
        //int[] originalSeq=new int [encodedMatrix[0].length*m+1];
        int[][] encodedShares = new int[m][encodedMatrix[0].length];
        int index = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < encodedMatrix[0].length; j++) {
                encodedShares[i][j] = encodedMatrix[labels[i]][j];
            }
        }

        inverseMatrix(labels);

        for (int i = 0; i < encodedMatrix[0].length; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < m; k++) {
                    decodedSeq[index] = addInGF256(decodedSeq[index],
                            mulsInGF256[encodedShares[k][i]][inverseVandermonde[j][k]]);
                }
                index++;
            }
        }

        //originalSeq=new int[decodedSeq.length+1];
        //	for (int x=0; x<decodedSeq.length; x++)
        //           { System.out.print(decodedSeq[x]);
        //originalSeq[x]=(decodedSeq[x+1]);
        //     }
        //	
        return decodedSeq;
    }

    //Compute matrix multiplicative inverse
    public void inverseMatrix(int[] labels) {
        int[][] tmpMatrix = new int[m][m];

        //clone matrix
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                tmpMatrix[i][j] = vandermonde[labels[i]][j];
            }
        }

        // initialize inverse matrix as unit matrix
        for (int i = 0; i < m; i++) {
            inverseVandermonde[i][i] = 1;
        }

        //simultaneously compute Gaussian reduction of tmpMatrix and unit matrix
        for (int i = 0; i < m; i++) {

            // normalize i-th row
            int coef = tmpMatrix[i][i];
            int invCoef = inverse[coef];
            multRowWithElementThis(tmpMatrix[i], invCoef);
            multRowWithElementThis(inverseVandermonde[i], invCoef);

            // normalize all other rows
            for (int j = 0; j < tmpMatrix.length; j++) {
                if (j != i) {
                    coef = tmpMatrix[j][i];
                    if (coef != 0) {
                        int[] tmpRow = multRowWithElement(tmpMatrix[i], coef);
                        int[] tmpInvRow = multRowWithElement(inverseVandermonde[i], coef);
                        addToRow(tmpRow, tmpMatrix[j]);
                        addToRow(tmpInvRow, inverseVandermonde[j]);
                    }

                }
            }
        }
    }

    //addition in GF(2^8)
    public int addInGF256(int a, int b) {
        return a ^ b;
    }

    // Multiplication in GF(2^8)
    public int mulInGF256(int a, int b) {
        int p = 0;
        for (; b != 0; b >>= 1) {
            p ^= (a * (b & 1));
            a = ((a >> 7) == 1 ? ((a << 1) ^ 0x1B) : (a << 1)) & 0xFF;
        }
        return p;
    }

    private void multRowWithElementThis(int[] row, int element) {
        for (int i = 0; i < row.length; i++) {
            row[i] = mulsInGF256[row[i]][element];
        }

    }

    private int[] multRowWithElement(int[] row, int element) {
        int[] result = new int[row.length];
        for (int i = 0; i < row.length; i++) {
            result[i] = mulsInGF256[row[i]][element];
        }
        return result;
    }

    private void addToRow(int[] fromRow, int[] toRow) {
        for (int i = 0; i < toRow.length; i++) {
            toRow[i] = addInGF256(fromRow[i], toRow[i]);
        }

    }

    public void printInverse(int element) {

        System.out.print(inverse[element]);
    }

//	public void vandermondeInverse(){
//		int[] temp1= new int [m+1];
//		int[] temp2= new int [m+1];
//		int[] temp3=temp1;
//		
//		for (int i=1; i<m; i++){
//			for (int j=0; j<=i+1; j++)
//				temp2[j]=addInGF256(j>0?temp1[j-1]:0, mulsInGF256[][j<=i?temp1[j]:0]);
//			
//			for (int k=0; k<i+2; k++)
//				temp1[k]=temp2[k];
//		}
//		
//		for (int x=0; x<m; x++){
//			int p=; 
//			int q=inverseVandermonde[m-1][x]=1;
//			int r=temp3[1];
//			
//			for (int i=1; i<m; i++, p=mulsInGF256[p][]){
//				q=inverseVandermonde[m-1-i][x]=addInGF256(mulsInGF256[][q],temp3[m-i]);
//				if ((i&1)==0)
//					r=addInGF256(r,mulsInGF256[p][temp3[i+1]]);
//			}
//			
//			for (int y=0; y<m; y++)
//				inverseVandermonde[y][x]=mulsInGF256[inverseVandermonde[y][x]][inverse[r]];
//		}
//	}
//	public int[][] encode(int[] message){
//		int[] temp;
//		int[][] vanderMatrix = new int [n][m];
//				
//		//checks and deals with a message which its size is not multiplication of m
//		if (message.length%m!=0){
//			temp = new int [message.length+m-(message.length%m)];
//			for (int i=0; i<temp.length;i++)
//				temp[i]=(i<message.length?message[i]:0);
//		}
//		else
//			temp=message;
//		
//		encodedMatrix= new int [n][temp.length/m];
//		
//		//convert the numbers to be in p field
//		for (int i=0;i<temp.length; i++)
//			temp[i]%=p;
//		
//		//builds the vandermonde matrix (the encoding matrix)
//		for (int i=0; i<n; i++){
//			for (int j=0;j<m; j++)
//				vanderMatrix[i][j]=(int) Math.pow(i+1, j);
//		}
//		
//		//encode the message
//		for (int i=0; i<n; i++){
//			for (int j=0; j<temp.length/m; j++){
//				for (int k=0; k<m; k++){
//					encodedMatrix[i][j]+=vanderMatrix[i][k]*temp[k+m*j];
//					encodedMatrix[i][j]%=p;
//				}
//			}
//		}
//		
//		return encodedMatrix;
//	}
//	
//	//gets the fragments ID for the reconstruction
//	public void setLabels (int[] fragID){
//		labels=fragID;
//	}
//	
//	
//	
//	
//	public int[] decode(){
//		double [][] encodingMatrix= new double [m][m];
//		double[][] decodedMatrix= new double [m][m];
//		int[][] frag= new int [encodedMatrix[0].length][encodedMatrix[0].length];
//		int[] oMessage= new int [frag[0].length*frag.length];
//		
//		for (int i=0; i<frag.length; i++)
//			for (int j=0; j<frag.length; j++)
//				frag[i][j]=encodedMatrix[labels[i]][j];
//		
//		//set matrix B
//		for (int i=0; i<m; i++){
//			for (int j=0;j<m; j++)
//				encodingMatrix[i][j]= Math.pow(labels[i]+1, j);
//		}
//		
//		//set inverse matrix of B
////		Matrix inverseEncodingMatrix= new Matrix(encodingMatrix);
//		
//		
//		
//		for (int i=0; i<m; i++)
//			for (int j=0; j<m; j++){
//				for (int k=0; k<m; k++){
//					decodedMatrix[i][j]+=iMatrix.get(j,k)*frag[k][i];
//				}
//				oMessage[i*m+j]=(decodedMatrix[i][j]<0)?(int) decodedMatrix[i][j]%p+p:(int) decodedMatrix[i][j]%p;
//				
//			}	
//			
//		
//		return oMessage;
//	}
}
