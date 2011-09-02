/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pratica01;
//class RTPpacket

public class RTPpacket{

  //size of the RTP header:
  static int HEADER_SIZE = 12;

  //Fields that compose the RTP header
  public int Version;
  public int Padding;
  public int Extension;
  public int CC;
  public int Marker;
  public int PayloadType;
  public int SequenceNumber;
  public int TimeStamp;
  public int Ssrc;

  //Bitstream of the RTP header
  public byte[] header;

  //size of the RTP payload
  public int payload_size;
  //Bitstream of the RTP payload
  public byte[] payload;



  //--------------------------
  //Constructor of an RTPpacket object from header fields and payload bitstream
  //--------------------------
  public RTPpacket(int PType, int Framenb, int Time, byte[] data, int data_length){
    //fill by default header fields:
    Version = 2;
    Padding = 0;
    Extension = 0;
    CC = 0;
    Marker = 0;
    Ssrc = 0;

    //fill changing header fields:
    SequenceNumber = Framenb;
    TimeStamp = Time;
    PayloadType = PType;

    //build the header bistream:
    //--------------------------
    header = new byte[HEADER_SIZE];

    //.............
    //TO COMPLETE
    //.............
    //Fill the header array of byte with RTP header fields
    //----------------------------------------------------
    
    /*      
     * bits 0-1: Version
     * bit    2: Padding 
     * bit    3: Extension
     * bits 4-7: CC
     */
    header[0]=(byte)((Version << 6)|(Padding << 5)|(Extension << 6)|CC);
    
    /* 
     * bit    0: Marker
     * bits 1-7: PayloadType
     */
    header[1] = (byte)( (Marker << 7)|PayloadType );
    
    /* SequenceNumber takes 2 bytes = 16 bits */
    header[2] = (byte)( SequenceNumber >> 8 );
    header[3] = (byte)( SequenceNumber );
    
    /* TimeStamp takes 4 bytes = 32 bits */
    for ( int i = 0; i < 4; i++ )
        header[7-i] = (byte)( TimeStamp >> (8*i) );

    /* Ssrc takes 4 bytes = 32 bits */
    for ( int i = 0; i < 4; i++ )
        header[11-i] = (byte)( Ssrc >> (8*i) );

    //fill the payload bitstream:
    //--------------------------
    payload_size = data_length;
    payload = new byte[data_length];

    //fill payload array of byte from data (given in parameter of the constructor)
    payload = data;

    // ! Do not forget to uncomment method printheader() below !

  }

  //--------------------------
  //Constructor of an RTPpacket object from the packet bistream
  //--------------------------
  public RTPpacket(byte[] packet, int packet_size)
  {
    //fill default fields:
    Version = 2;
    Padding = 0;
    Extension = 0;
    CC = 0;
    Marker = 0;
    Ssrc = 0;

    //check if total packet size is lower than the header size
    if (packet_size >= HEADER_SIZE)
      {
	//get the header bitsream:
	header = new byte[HEADER_SIZE];
	for (int i=0; i < HEADER_SIZE; i++)
	  header[i] = packet[i];

	//get the payload bitstream:
	payload_size = packet_size - HEADER_SIZE;
	payload = new byte[payload_size];
	for (int i=HEADER_SIZE; i < packet_size; i++)
	  payload[i-HEADER_SIZE] = packet[i];

	//interpret the changing fields of the header:
	PayloadType = header[1] & 127;
	SequenceNumber = unsigned_int(header[3]) + 256*unsigned_int(header[2]);
	TimeStamp = unsigned_int(header[7]) + 256*unsigned_int(header[6]) + 65536*unsigned_int(header[5]) + 16777216*unsigned_int(header[4]);
      }
 }

  //--------------------------
  //getpayload: return the payload bistream of the RTPpacket and its size
  //--------------------------
  public int getpayload(byte[] data) {

    for (int i=0; i < payload_size; i++)
      data[i] = payload[i];

    return(payload_size);
  }

  //--------------------------
  //getpayload_length: return the length of the payload
  //--------------------------
  public int getpayload_length() {
    return(payload_size);
  }

  //--------------------------
  //getlength: return the total length of the RTP packet
  //--------------------------
  public int getlength() {
    return(payload_size + HEADER_SIZE);
  }

  //--------------------------
  //getpacket: returns the packet bitstream and its length
  //--------------------------
  public int getpacket(byte[] packet)
  {
    //construct the packet = header + payload
    for (int i=0; i < HEADER_SIZE; i++)
	packet[i] = header[i];
    for (int i=0; i < payload_size; i++)
	packet[i+HEADER_SIZE] = payload[i];

    //return total size of the packet
    return(payload_size + HEADER_SIZE);
  }

  //--------------------------
  //gettimestamp
  //--------------------------

  public int gettimestamp() {
    return(TimeStamp);
  }

  //--------------------------
  //getsequencenumber
  //--------------------------
  public int getsequencenumber() {
    return(SequenceNumber);
  }

  //--------------------------
  //getpayloadtype
  //--------------------------
  public int getpayloadtype() {
    return(PayloadType);
  }


  //--------------------------
  //print headers without the SSRC
  //--------------------------
	public void printheader()
  {
    for (int i=0; i < (HEADER_SIZE-4); i++)
      {
	for (int j = 7; j>=0 ; j--)
	  if (((1<<j) & header[i] ) != 0)
	    System.out.print("1");
	else
	  System.out.print("0");
	System.out.print(" ");
      }

    System.out.println();
  }


  //return the unsigned value of 8-bit integer nb
  static int unsigned_int(int nb) {
    if (nb >= 0)
      return(nb);
    else
      return(256+nb);
  }
}
  //--------------------------
  //print headers without the SSRC
  //--------------------------
//  public void printheader()
//  {
//    //TO DO: uncomment
//    /*
//    for (int i=0; i < (HEADER_SIZE-4); i++)
//      {
//	for (int j = 7; j>=0 ; j--)
//	  if (((1<= 0)
//      return(nb);
//    else
//      return(256+nb);
//  }
//}
