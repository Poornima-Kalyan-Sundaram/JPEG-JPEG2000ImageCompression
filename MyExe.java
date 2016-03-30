
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class MyExe {
	JFrame frame;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage img;
	static BufferedImage imgdct;
	static BufferedImage imgdwt;
	static int presetrun=0;
	public int N=8;
    /* initialize coefficient matrix */
    protected void initCoefficients() {
        c = new double[8][8];

        for (int i=1;i<N;i++) {
        	for (int j=1;j<8;j++) {
        		c[i][j]=1;
        	}
        }

        for (int i=0;i<8;i++) {
                c[i][0]=1/Math.sqrt(2.0);
        	c[0][i]=1/Math.sqrt(2.0);
        }
        c[0][0]=0.5;
    }
	/*compute dct
	*/
    protected double[][] dct(double[][] input) {
        double[][] output = new double[8][8];

        for (int u=0;u<8;u++) {
          for (int v=0;v<8;v++) {
            double sum = 0.0;
            for (int x=0;x<8;x++) {
              for (int y=0;y<8;y++) {
                sum+=input[x][y]*Math.cos(((2*x+1)/(2.0*8))*u*Math.PI)*Math.cos(((2*y+1)/(2.0*8))*v*Math.PI);
              }
            }
            sum*=c[u][v]/4.0;
            output[u][v]=sum;
          }
        }
        return output;
    }
	/*
	compute IDCT
	*/
    protected double[][] idct(double[][] input) {
       double[][] output = new double[8][8];

       for (int x=0;x<8;x++) {
        for (int y=0;y<8;y++) {
          double sum = 0.0;
          for (int u=0;u<8;u++) {
            for (int v=0;v<8;v++) {
            sum+=c[u][v]*input[u][v]*Math.cos(((2*x+1)/(2.0*8))*u*Math.PI)*Math.cos(((2*y+1)/(2.0*8))*v*Math.PI);
            }
          }
          sum/=4.0;
          output[x][y]=sum;
        }
       }
       return output;
    }

    public double c[][]        = new double[8][8];
    public double cT[][]        = new double[8][8];


	public void rowdwt(int left,int right,double []arr)
	{
			if(right-left<2)
			return;
			else
			{

				double []hp=new double[512];
				double []lp=new double[512];

				int mid=(left+right)/2;

				int ind=(right-left)/2;
				int j=left;
				for(int k=0;k<ind;k++,j+=2)
				{hp[k]=(arr[j]+arr[j+1])/2;}
				j=left;
				for(int k=0;k<ind;k++,j+=2)
				{lp[k]=(arr[j]-arr[j+1])/2;}
				j=left;int k=0;
				while(j<mid)
				{
					arr[j]=hp[k];
					j++;
					k++;
				}
				k=0;
				while(j<right)
				{
					arr[j]=lp[k];
					j++;
					k++;

				}


		

				rowdwt(left,mid,arr);
				rowdwt(mid,right,arr);
		
			}

	}



void rowidwt(int left,int right,double[]arr)
{
	if(right-left<2)
	return;
	else
	{

		int mid=(left+right)/2;

		
		
		rowidwt(left,mid,arr);
		rowidwt(mid,right,arr);
		double[]hp=new double [512];		
		double[]lp=new double [512];		

		int ind=(right-left)/2;
		int j=left;
		j=left;
		for(int k=0;k<ind;k++,j++)
		{hp[k]=(arr[j]+arr[j+ind]);}
		j=left;
		for(int k=0;k<ind;k++,j++)
		{lp[k]=(arr[j]-arr[j+ind]);}
		j=left;int k=0;

		//alternate placement of highpass and lowpass
		j=left;k=0;
		while(k<ind)
		{
			arr[j]=hp[k];
			k++;
			j+=2;
		}
		k=0;
		j=left+1;
		while(k<ind)
		{
			arr[j]=lp[k];
			k++;
			j+=2;
		}
	}
}
	
	public void showIms(String[] args) throws Exception{
		int width = 512;//Integer.parseInt(args[1]);
		int height = 512;//Integer.parseInt(args[2]);
		int coefficient = Integer.parseInt(args[1]);		
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imgdct = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imgdwt = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		try {
			initCoefficients();
			//initMatrix();
			File file = new File(args[0]);
			InputStream is = new FileInputStream(file);

			long len = file.length();
			byte[] bytes = new byte[(int)len];

			int[][] original_red = new int[height][width];			
			int[][] original_blue = new int[height][width];			
			int[][] original_green = new int[height][width];			

			double[][] dct_red = new double[height][width];			
			double[][] dct_blue = new double[height][width];			
			double[][] dct_green = new double[height][width];			

			double[][] idct_red = new double[height][width];			
			double[][] idct_blue = new double[height][width];			
			double[][] idct_green = new double[height][width];			


			double[][] dct_red_int = new double[height][width];			
			double[][] dct_blue_int = new double[height][width];			
			double[][] dct_green_int = new double[height][width];			

				double[][] dwt_red = new double[height][width];			
				double[][] dwt_blue = new double[height][width];			
				double[][] dwt_green = new double[height][width];			

				double[][] idwt_red = new double[height][width];			
				double[][] idwt_blue = new double[height][width];			
				double[][] idwt_green = new double[height][width];			


				double[][] dwt_red_int = new double[height][width];			
				double[][] dwt_blue_int = new double[height][width];			
				double[][] dwt_green_int = new double[height][width];			


			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			int ind = 0;
			for(int y = 0; y < height; y++){

				for(int x = 0; x < width; x++){

					byte a = 0;
					int r = bytes[ind]&0x000000ff;
					int g = bytes[ind+height*width]&0x000000ff;
					int b = bytes[ind+height*width*2]&0x000000ff; 
					if(r<0)
						r=0;
					if(g<0)
						g=0;
					if(b<0)
						b=0;
					
					if(r>255)
						r=255;
					if(g>255)
						g=255;
					if(b>255)
						b=255;
					r-=127;
					g-=127;
					b-=127;

					original_red[y][x]=r;
					original_green[y][x]=g;
					original_blue[y][x]=b;

					dct_red[y][x]=r;
					dct_green[y][x]=g;
					dct_blue[y][x]=b;

					dwt_red[y][x]=r;
					dwt_green[y][x]=g;
					dwt_blue[y][x]=b;
					
					r+=127;
					g+=127;
					b+=127;
					
					
					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					img.setRGB(x,y,pix);
					ind++;
				}
			}

/*=========================================DCT ENCODING=============================================================*/
			/********************block by block traversal*********************/

			int height_index=0;
			int width_index=0;
			
			//confirm U and v values in DCT!!?!!

			//DCT parameters
			double c_u,c_v;
			int x_index,y_index;
			
			//FIND DCT RED
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=original_red[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=dct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							dct_red[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}

			//FIND DCT green
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=original_green[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=dct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							dct_green[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}

			//FIND DCT blue
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=original_blue[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=dct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							dct_blue[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}



			//initialise the intermediate decoder matrix
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					dct_green_int[y][x]=0;
					dct_red_int[y][x]=0;
					dct_blue_int[y][x]=0;

				}
			}


			//pick num_values=coefficient/4096
			int num_values=Math.round(coefficient/4096);

			//traversing in zig zag to pick num_values coefficients 
			if(num_values!=0)
			{
				for(int y = 0; y < height; y+=8)
				{
					for(int x = 0; x < width; x+=8)
					{
						//dc coefficient
					
						int x_block_end=x+8;
						int y_block_end=y+8;
						int counter=0;
						int flag=0;				

						y_index=y;
						x_index=x;
					int i=0,j=0;

					int up=0,down=0;

					//upper triangle with the main diagonal
					//System.out.println(i+" "+j);
					counter++;
					dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
					dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
					dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

					if(counter==num_values)
					{
						continue;
					}

					do
					{
						if(i==0&&j%2==0)
						{
							//go right if you can
							if(j==7)
								break;
							else
							{
								j++;
								//System.out.println(i+" "+j);
								counter++;
								dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
								dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
								dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

								if(counter==num_values)
								{
									flag=1;
									break;
								}

								down=1;up=0;
							}
						}
						else if(i%2!=0&&j==0)
						{
							if(i==7)
								break;
							else
							{
								i++;
								//System.out.println(i+" "+j);
								counter++;
								dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
								dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
								dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

								if(counter==num_values)
								{
									flag=1;
									break;
								}

								up=1;down=0;
							}
						}
						if(up==1)
						{
							i--;j++;
						}
						else
						{
							i++;j--;
						}
						//System.out.println(i+" "+j);
						counter++;		
						dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
						dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
						dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

						if(counter==num_values)
						{
							flag=1;
							break;
						}

					}while(flag==0);

					up=0;down=0;
					while(flag==0)
					{
						if(i==7&&j==7)
						break;
						else if(i==7&&j%2==0)
						{
							j++;
							if(j==7)
							break;
							//System.out.println(i+" "+j);
							counter++;
							dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
							dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
							dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];
							if(counter==num_values)
							{
								flag=1;
								break;
							}

							up=1;down=0;
						}

						else if(j==7&&i%2!=0)
						{
							i++;
							if(i==7)
							break;
							//System.out.println(i+" "+j);
							counter++;
							dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
							dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
							dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];
							if(counter==num_values)
							{
								flag=1;
								break;
							}

							down=1;up=0;
	
						}

						if(up==1)
						{
							i--;j++;
						}
						else
						{
							i++;j--;
						}
						//System.out.println(i+" "+j);
						counter++;		
						dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
						dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
						dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

						if(counter==num_values)
						{
							flag=1;
							break;
						}

					}
						//System.out.println(i+" "+j);
						counter++;
						if(counter==64)
						{
							dct_red_int[y+i][x+j]=dct_red[y+i][x+j];
							dct_green_int[y+i][x+j]=dct_green[y+i][x+j];
							dct_blue_int[y+i][x+j]=dct_blue[y+i][x+j];

						}
		
					}
				}

			}



			/****************************************************************/
			height_index=0;
			width_index=0;

			//DCT parameters
			 c_u=0;c_v=0;
			x_index=0;y_index=0;
			
			//FIND IDCT -RED
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=dct_red_int[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=idct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							idct_red[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}

			//FIND IDCT -RED
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=dct_green_int[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=idct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							idct_green[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}

			//FIND IDCT -RED
			for(int y = 0; y < height; y+=8)
			{
				for(int x = 0; x < width; x+=8)
				{
					//dc coefficient
					int x_block_end=x+8;
					int y_block_end=y+8;
					//copy contents to input array
					double [][]input=new double[8][8];
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							input[i][j]=dct_blue_int[y+i][x+j];
						}
					}
					double [][]output=new double[8][8];
					output=idct(input);
					for(int i=0;i<8;i++)
					{
						for(int j=0;j<8;j++)
						{
							idct_blue[y+i][x+j]=output[i][j];
						}
					}
					
					
				}
			}
			
			//convert idct to an array
			 ind = 0;
			for(int y = 0; y < height; y++){

				for(int x = 0; x < width; x++){

					byte a = 0;


					int r = ((int)idct_red[y][x]);
					int g = ((int)idct_green[y][x]);
					int b = ((int)idct_blue[y][x]); 
					r+=127;
					g+=127;
					b+=127;

					if(r<0)
						r=0;
					if(g<0)
						g=0;
					if(b<0)
						b=0;
					
					if(r>255)
						r=255;
					if(g>255)
						g=255;
					if(b>255)
						b=255;

					
					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					imgdct.setRGB(x,y,pix);
					ind++;
				}
			}
/**********************************************DWT conversion****************************************************************/

			for(int i=0;i<height;i++)
				{
					rowdwt(0,512,dwt_red[i]);
					rowdwt(0,512,dwt_green[i]);
					rowdwt(0,512,dwt_blue[i]);

				}
				//compute dwt for red,green and blue 
				for(int i=0;i<height;i++)
				{
					double []colvector=new double[512];
					int j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=dwt_red[x][i];
					}
					rowdwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						dwt_red[x][i]=colvector[j];
					}

					j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=dwt_green[x][i];
					}
					rowdwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						dwt_green[x][i]=colvector[j];
					}

					j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=dwt_blue[x][i];
					}
					rowdwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						dwt_blue[x][i]=colvector[j];
					}


				}


				num_values=Math.round(coefficient);

				//traversing in zig zag to pick num_values coefficients -for entire image
				if(num_values!=0)
				{
						
							int counter=0;
							int flag=0;				
						int i=0,j=0;

						int up=0,down=0;

						//upper triangle with the main diagonal
						//System.out.println(i+" "+j);
						counter++;
						dwt_red_int[i][j]=dwt_red[i][j];
						dwt_green_int[i][j]=dwt_green[i][j];
						dwt_blue_int[i][j]=dwt_blue[i][j];

						if(counter==num_values)
						{
							flag=1;
							
						}

						do
						{
							if(i==0&&j%2==0)
							{
								//go right if you can
								if(j==511)
									break;
								else
								{
									j++;
									//System.out.println(i+" "+j);
									counter++;
									dwt_red_int[i][j]=dwt_red[i][j];
									dwt_green_int[i][j]=dwt_green[i][j];
									dwt_blue_int[i][j]=dwt_blue[i][j];

									if(counter==num_values)
									{
										flag=1;
										break;
									}

									down=1;up=0;
								}
							}
							else if(i%2!=0&&j==0)
							{
								if(i==511)
									break;
								else
								{
									i++;
									//System.out.println(i+" "+j);
									counter++;
									dwt_red_int[i][j]=dwt_red[i][j];
									dwt_green_int[i][j]=dwt_green[i][j];
									dwt_blue_int[i][j]=dwt_blue[i][j];

									if(counter==num_values)
									{
										flag=1;
										break;
									}

									up=1;down=0;
								}
							}
							if(up==1)
							{
								i--;j++;
							}
							else
							{
								i++;j--;
							}
							//System.out.println(i+" "+j);
							counter++;		
							dwt_red_int[i][j]=dwt_red[i][j];
							dwt_green_int[i][j]=dwt_green[i][j];
							dwt_blue_int[i][j]=dwt_blue[i][j];

							if(counter==num_values)
							{
								flag=1;
								break;
							}

						}while(flag==0);

						up=0;down=0;
						while(flag==0)
						{
							if(i==511&&j==511)
							break;
							else if(i==511&&j%2==0)
							{
								j++;
								if(j==511)
								break;
								//System.out.println(i+" "+j);
								counter++;
								dwt_red_int[i][j]=dwt_red[i][j];
								dwt_green_int[i][j]=dwt_green[i][j];
								dwt_blue_int[i][j]=dwt_blue[i][j];
								if(counter==num_values)
								{
									flag=1;
									break;
								}

								up=1;down=0;
							}

							else if(j==511&&i%2!=0)
							{
								i++;
								if(i==511)
								break;
								//System.out.println(i+" "+j);
								counter++;
								dwt_red_int[i][j]=dwt_red[i][j];
								dwt_green_int[i][j]=dwt_green[i][j];
								dwt_blue_int[i][j]=dwt_blue[i][j];
								if(counter==num_values)
								{
									flag=1;
									break;
								}

								down=1;up=0;
		
							}

							if(up==1)
							{
								i--;j++;
							}
							else
							{
								i++;j--;
							}
							//System.out.println(i+" "+j);
							counter++;		
							dwt_red_int[i][j]=dwt_red[i][j];
							dwt_green_int[i][j]=dwt_green[i][j];
							dwt_blue_int[i][j]=dwt_blue[i][j];

							if(counter==num_values)
							{
								flag=1;
								break;
							}

						}
							//System.out.println(i+" "+j);
							counter++;
							if(counter==512)
							{
								dwt_red_int[i][j]=dwt_red[i][j];
								dwt_green_int[i][j]=dwt_green[i][j];
								dwt_blue_int[i][j]=dwt_blue[i][j];

							}

				}

				//copy dwt_int to idwt array
				for(int y=0;y<height;y++)
				{
					for(int x=0;x<width;x++)
					{
						idwt_red[y][x]=dwt_red_int[y][x];
						idwt_green[y][x]=dwt_green_int[y][x];
						idwt_blue[y][x]=dwt_blue_int[y][x];

					}
				}
				//compute idwt
				for(int i=0;i<height;i++)
				{
					double []colvector=new double[512];
					int j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=idwt_red[x][i];
					}
					rowidwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						idwt_red[x][i]=colvector[j];
					}

					j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=idwt_green[x][i];
					}
					rowidwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						idwt_green[x][i]=colvector[j];
					}

					j=0;
					for(int x=0;x<width;x++,j++)
					{
						colvector[j]=idwt_blue[x][i];
					}
					rowidwt(0,512,colvector);
					j=0;
					for(int x=0;x<width;x++,j++)
					{
						idwt_blue[x][i]=colvector[j];
					}


				}
				
					for(int i=0;i<height;i++)
					{
						rowidwt(0,512,idwt_red[i]);
						rowidwt(0,512,idwt_green[i]);
						rowidwt(0,512,idwt_blue[i]);
	
					}
				
			
			



				
					//convert idwt to an array
					 ind = 0;
					for(int y = 0; y < height; y++){

						for(int x = 0; x < width; x++){

							byte a = 0;


							int r = ((int)idwt_red[y][x]);
							int g = ((int)idwt_green[y][x]);
							int b = ((int)idwt_blue[y][x]); 
							r+=127;
							g+=127;
							b+=127;

							if(r<0)
								r=0;
							if(g<0)
								g=0;
							if(b<0)
								b=0;
							
							if(r>255)
								r=255;
							if(g>255)
								g=255;
							if(b>255)
								b=255;

							
							int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
							//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
							imgdwt.setRGB(x,y,pix);
							ind++;
						}
					}
			
/*===================================================================================================================*/

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Use labels to display the images
		if(presetrun!=1)
		{
			frame = new JFrame();
			GridBagLayout gLayout = new GridBagLayout();
			frame.getContentPane().setLayout(gLayout);

			JLabel lbText1 = new JLabel("Image after modification (DWT)");
			lbText1.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel lbText2 = new JLabel("Image after modification (DCT)");
			lbText2.setHorizontalAlignment(SwingConstants.CENTER);
			lbIm1 = new JLabel(new ImageIcon(imgdwt));
			lbIm2 = new JLabel(new ImageIcon(imgdct));

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.weightx = 0.5;
			c.gridx = 0;
			c.gridy = 0;
			frame.getContentPane().add(lbText1, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.weightx = 0.5;
			c.gridx = 1;
			c.gridy = 0;
			frame.getContentPane().add(lbText2, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 1;
			frame.getContentPane().add(lbIm1, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			frame.getContentPane().add(lbIm2, c);

			frame.pack();
			frame.setVisible(true);
			
		}

	    }		
	

	public static void main(final String[] args) throws Exception {
		final MyExe ren = new MyExe();
		final int x=4096;

		int coefficient=Integer.parseInt(args[1]);
		if(coefficient!=-1)
		{
			ren.showIms(args);
		}
		else
		{
			presetrun=1;
/*			 ActionListener taskPerformer = new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			        //...Perform a task...
			    	
*/
				for(int i=4096;i<=262144;i+=4096)
				{
		    			try {
							args[1]=String.valueOf(i);
							ren.showIms(args);
							JFrame frame;
							JLabel lbIm1;
							JLabel lbIm2;

							frame = new JFrame();
							GridBagLayout gLayout = new GridBagLayout();
							frame.getContentPane().setLayout(gLayout);
							String s=String.valueOf(i);
							JLabel lbText1 = new JLabel("Image after modification (DWT) with m="+s);
							lbText1.setHorizontalAlignment(SwingConstants.CENTER);
							JLabel lbText2 = new JLabel("Image after modification (DCT) with m="+s);
							lbText2.setHorizontalAlignment(SwingConstants.CENTER);
							lbIm1 = new JLabel(new ImageIcon(imgdwt));
							lbIm2 = new JLabel(new ImageIcon(imgdct));

							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							c.anchor = GridBagConstraints.CENTER;
							c.weightx = 0.5;
							c.gridx = 0;
							c.gridy = 0;
							frame.getContentPane().add(lbText1, c);

							c.fill = GridBagConstraints.HORIZONTAL;
							c.anchor = GridBagConstraints.CENTER;
							c.weightx = 0.5;
							c.gridx = 1;
							c.gridy = 0;
							frame.getContentPane().add(lbText2, c);

							c.fill = GridBagConstraints.HORIZONTAL;
							c.gridx = 0;
							c.gridy = 1;
							frame.getContentPane().add(lbIm1, c);

							c.fill = GridBagConstraints.HORIZONTAL;
							c.gridx = 1;
							c.gridy = 1;
							frame.getContentPane().add(lbIm2, c);

							frame.pack();
							frame.setVisible(true);
							
							Thread.sleep(5000);
							frame.setVisible(false);
							frame.dispose();
							
							//
							//frame.setVisible(false);
							//frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
		    			
			  //  }
			//};
			//in milliseconds
			//Timer timer = new Timer(1000 ,taskPerformer);
			//timer.setRepeats(true);
			//timer.start();
			//Thread.sleep(5000);

		}
	}

}
