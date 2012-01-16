package test;

import java.util.ArrayList;
import jayu.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_EBlock implements TestConst {

	public byte[] getByteArr(String fileName) {
		TimeStamp ts = new TimeStamp();
		byte[] byteArr = null;
		try {
			byteArr = Util.FileToByteArray(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(byteArr != null && byteArr.length > 1);
		System.out.printf(
				"\n Time to read %d bytes ASN Data in memory: %d ms ",
				byteArr.length, ts.elapsedMS());
		return byteArr;
	}
	
	@Test
	public void getBlockZte() {
		System.out.printf("\n Start of Block.getBlockZte() Test =================");
		byte[] byteArr = getByteArr(ZTE_DATA_FILE);
        //                    T  L  filebytes  offset valStart valEnd
		EBlock b = new EBlock(0, 0, byteArr,   0     ,0       ,byteArr.length);
		int zteBlockSize = 2048;
		byte ztePaddingByte = (byte) 0xFF;
		ArrayList<EBlock> arr = b.getSubBlocks(EBlock.MAX_BLOCKS, zteBlockSize, ztePaddingByte);
		assertTrue(arr.size() == 14);
		System.out.printf( "\n ZTE blocksize(%d)  paddingByte(%s)",zteBlockSize, ztePaddingByte);
		System.out.printf( "\n Number of Top level Blocks in ZTE data file is %d", arr.size());
	}

	public void getBlockHua() {
		
		// ORIGINAL FILE  
		// TAG : A1   (offset 23)
		// LENGTH: 83 15 77 BA  // 83 indicates 3 more bytes used for length
		// VALUE: .....
		// LENGTH OF TOP LEVEL = 83 15 77 FF // = (18) + 1+4+LENGTH(1406906) = 15 77 D1   + (44) (2) ||||  69 + 1406906  
		
		// MODIFIED FILE (SMALL)
		// TAG : A1
		// LENGTH: ??   (JUST BIG ENOUGH TO HOLD ONLY 3 CDRS)
		//        CDR1 = 1+2+185=188
		//        CDR2 = 1+3+532=536
		//        CDR3 = 1+3+293=297
		//        Total =       1021   =  HEX[ 83 00 03 FD]
		// LENGTH: 83 00 03 FD	
		// LENGTH OF TOP LEVEL = (18) + 1+4+LENGTH(1021) +  (44) + (2) =  |||| 69 + 1021 = 1090 >>  83 00 04 42
		
	}

	@Test
	public void getBlockAlu() {
		System.out.printf("\n Start of Block.getBlockAlu() Test");
		byte[] byteArr = getByteArr(ALU_DATA_FILE);

		EBlock b = EBlock.getBlock(byteArr, 0 ,0,(byte) 0);

		// First CDR
		assertTrue(b.getTag() == 1 && b.getLength() == 200
				&& b.getFileOffset() == 0 && b.getBlockSize() == 203
				&& b.getNextBlockOffset() == 203 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 2 && b.getValStart() == 3
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == false);

		ArrayList<EBlock> sb = b.getSubBlocks(EBlock.MAX_BLOCKS ,0,(byte) 0 );
		EBlock sb_recEntity = sb.get(5); // 5th subNode has tag '6'
											// corresponding to recording
											// Entity.
		assertTrue(sb_recEntity.getTag() == 6 && sb_recEntity.getLength() == 6
				&& sb_recEntity.getFileOffset() == 44
				&& sb_recEntity.getBlockSize() == 8
				&& sb_recEntity.getNextBlockOffset() == 52
				&& sb_recEntity.getNumTagBytes() == 1
				&& sb_recEntity.getNumLenBytes() == 1
				&& sb_recEntity.getValStart() == 46
				&& sb_recEntity.isLeaf() == true
				&& sb_recEntity.isUniversal() == false
				&& sb_recEntity.isIndefinateLength() == false);

		EBlock sb_mscIncomingTKGP = sb.get(6); // 6th subNode has tag '7'
												// corresponding to
												// mscIncomingTKGP.

		assertTrue(sb_mscIncomingTKGP.getTag() == 7
				&& sb_mscIncomingTKGP.getLength() == 5
				&& sb_mscIncomingTKGP.getFileOffset() == 52
				&& sb_mscIncomingTKGP.getBlockSize() == 7
				&& sb_mscIncomingTKGP.getNextBlockOffset() == 59
				&& sb_mscIncomingTKGP.getNumTagBytes() == 1
				&& sb_mscIncomingTKGP.getNumLenBytes() == 1
				&& sb_mscIncomingTKGP.getValStart() == 54
				&& sb_mscIncomingTKGP.isLeaf() == false
				&& sb_mscIncomingTKGP.isUniversal() == false
				&& sb_mscIncomingTKGP.isIndefinateLength() == false);

		ArrayList<EBlock> ssb = sb_mscIncomingTKGP
				.getSubBlocks(EBlock.MAX_BLOCKS,0,(byte) 0);
		EBlock sb_mscIncomingTKGP_tkgpNumber = ssb.get(0);
		// System.out.printf("\n sb_mscIncomingTKGP_tkgpNumber = %s",
		// sb_mscIncomingTKGP_tkgpNumber );

		assertTrue(sb_mscIncomingTKGP_tkgpNumber.getTag() == 0
				&& sb_mscIncomingTKGP_tkgpNumber.getLength() == 3
				&& sb_mscIncomingTKGP_tkgpNumber.getFileOffset() == 54
				&& sb_mscIncomingTKGP_tkgpNumber.getBlockSize() == 5
				&& sb_mscIncomingTKGP_tkgpNumber.getNextBlockOffset() == 59
				&& sb_mscIncomingTKGP_tkgpNumber.getNumTagBytes() == 1
				&& sb_mscIncomingTKGP_tkgpNumber.getNumLenBytes() == 1
				&& sb_mscIncomingTKGP_tkgpNumber.getValStart() == 56
				&& sb_mscIncomingTKGP_tkgpNumber.isLeaf() == true
				&& sb_mscIncomingTKGP_tkgpNumber.isUniversal() == false
				&& sb_mscIncomingTKGP_tkgpNumber.isIndefinateLength() == false);

		// ========================================
		// Second CDR
		b = EBlock.getBlock(byteArr, b.getNextBlockOffset(),0,(byte) 0);

		assertTrue(b.getTag() == 6 && b.getLength() == 151
				&& b.getFileOffset() == 203 && b.getBlockSize() == 154
				&& b.getNextBlockOffset() == 357 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 2 && b.getValStart() == 206
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == false);

		// Third CDR
		b = EBlock.getBlock(byteArr, b.getNextBlockOffset(),0,(byte) 0);

		assertTrue(b.getTag() == 0 && b.getLength() == 263
				&& b.getFileOffset() == 357 && b.getBlockSize() == 267
				&& b.getNextBlockOffset() == 624 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 3 && b.getValStart() == 361
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == false);

		// SubField in a Field of Third CDR
		b = EBlock.getBlock(byteArr, 528,0,(byte) 0);

		assertTrue(b.getTag() == 16 && b.getLength() == 27
				&& b.getFileOffset() == 528 && b.getBlockSize() == 29
				&& b.getNextBlockOffset() == 557 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 1 && b.getValStart() == 530
				&& b.isLeaf() == false && b.isUniversal() == true
				&& b.isIndefinateLength() == false);

	}

	@Test
	public void getBlockEri() {
		System.out.printf("\n Start of Block.getBlockEri() Test");
		byte[] byteArr = getByteArr(ERI_DATA_FILE);

		// First CDR ==========
		EBlock b = EBlock.getBlock(byteArr, 0,0,(byte) 0);

		assertTrue(b.getTag() == 1 && b.getLength() == 260
				&& b.getFileOffset() == 0 && b.getBlockSize() == 262
				&& b.getNextBlockOffset() == 262 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 1 && b.getValStart() == 2
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == true);

		// Second CDR =============
		b = EBlock.getBlock(byteArr, b.getNextBlockOffset(),0,(byte) 0);

		assertTrue(b.getTag() == 1 && b.getLength() == 440
				&& b.getFileOffset() == 262 && b.getBlockSize() == 442
				&& b.getNextBlockOffset() == 704 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 1 && b.getValStart() == 264
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == true);

		// Inside 2nd CDR ===========
		ArrayList<EBlock> subBlocks = b.getSubBlocks(EBlock.MAX_BLOCKS,0,(byte) 0);
		assertTrue(subBlocks.size() == 2);
		EBlock b0 = subBlocks.get(0);
		EBlock b1 = subBlocks.get(1);

		assertTrue(b0.getTag() == 0);
		assertTrue(b0.getLength() == 313);
		assertTrue(b0.getFileOffset() == 264);
		assertTrue(b0.getNextBlockOffset() == 581);
		assertTrue(b0.getNumTagBytes() == 1 && b0.getNumLenBytes() == 3);
		assertTrue(b0.isIndefinateLength() == false);

		assertTrue(b1.getTag() == 0);
		assertTrue(b1.getLength() == 119);
		assertTrue(b1.getNextBlockOffset() == 702);

		EBlock b00 = EBlock.getBlock(byteArr, b0.getValStart(),0,(byte) 0);
		assertTrue(b00.getFileOffset() == 268 && b00.getLength() == 309
				&& b00.getNextBlockOffset() == 581);

		subBlocks = b00.getSubBlocks(EBlock.MAX_BLOCKS,0,(byte) 0);
		EBlock b000 = subBlocks.get(0);
		assertTrue(b000.getFileOffset() == 272 && b000.getLength() == 1
				&& b000.getTag() == 33 && b000.getNextBlockOffset() == 276
				&& b000.isLeaf() == true);

		// 4th CDR (Definite Length) =========================
		b = EBlock.getBlock(byteArr, 966,0,(byte) 0);

		assertTrue(b.getTag() == 0 && b.getLength() == 193
				&& b.getFileOffset() == 966 && b.getBlockSize() == 196
				&& b.getNextBlockOffset() == 1162 && b.getNumTagBytes() == 1
				&& b.getNumLenBytes() == 2 && b.getValStart() == 969
				&& b.isLeaf() == false && b.isUniversal() == false
				&& b.isIndefinateLength() == false);
	}

	@Test
	public void getAllBlockEri() {
		System.out.printf("\n Test: getAllBlockEri(): ");
		byte[] byteArr = getByteArr(ERI_DATA_FILE);

		EBlock b = new EBlock(0, 0, byteArr, 0, 0, byteArr.length);
		ArrayList<EBlock> arr = b.getSubBlocks(EBlock.MAX_BLOCKS,0,(byte) 0);
		//assertTrue(arr.size() == 20464); // In original file.
		assertTrue(arr.size() == 5 ); // In modified smaller file		
		System.out.printf(
				"\n Number of Top level Blocks in Ericson data file is %d",
				arr.size());		

	}

}
