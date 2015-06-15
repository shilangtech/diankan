#include "memory.h"
#include "g72x.h"
#include "g726.h"
#include <stdlib.h>

void g726_40bit_Encode(unsigned char *speech,char *bitstream)
{
	g726_state state_ptr;
	short temp[480];
	int i;

	g726_init_state(&state_ptr);

	memcpy(temp,speech,960);

	for(i=0;i<60;i++)
	{
		char a0 = (char)(g726_32_encoder(temp[i*8],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a1 = (char)(g726_32_encoder(temp[i*8+1],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a2 = (char)(g726_32_encoder(temp[i*8+2],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a3 = (char)(g726_32_encoder(temp[i*8+3],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a4 = (char)(g726_32_encoder(temp[i*8+4],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a5 = (char)(g726_32_encoder(temp[i*8+5],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a6 = (char)(g726_32_encoder(temp[i*8+6],AUDIO_ENCODING_LINEAR,&state_ptr));
		char a7 = (char)(g726_32_encoder(temp[i*8+7],AUDIO_ENCODING_LINEAR,&state_ptr));
		*(bitstream+i*5)=a0<<3|a1>>2;
		*(bitstream+i*5+1)=a1<<6|a1<<1|a3>>4;
		*(bitstream+i*5+2)=a3<<4|a4>>1;
		*(bitstream+i*5+3)=a4<<7|a5<<2|a6>>3;
		*(bitstream+i*5+4)=a6<<5|a7;
	}
}

void g726_40bit_Decode(char *bitstream,unsigned char *speech)
{
	g726_state state_ptr;
	int i;
	int in;
	short temp;

	g726_init_state(&state_ptr);
	for(i=0;i<60;i++)
	{
		char a0=*(bitstream+i*5);
		char a1=*(bitstream+i*5+1);
		char a2=*(bitstream+i*5+2);
		char a3=*(bitstream+i*5+3);
		char a4=*(bitstream+i*5+4);
        
		in=(int)((a0&(char)248)>>3);
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16,&temp,2);

		in=(int)(((a0&(char)7)<<2)|((a1>>6)&(char)3));
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+2,&temp,2);

		in=(int)(((a1<<2)&(char)248)>>3);
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+4,&temp,2);

		in=(int)(((a1&(char)1)<<4)|((a2>>4)&(char)15));
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+6,&temp,2);

		in=(int)((((a2>>4)&(char)15)>>1)|((a3>>7)&(char)1));
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+8,&temp,2);

		in=(int)(((a3<<1)&248)>>3);
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+10,&temp,2);

		in=(int)(((a3&(char)3)<<3)|((a4>>5)&(char)7));
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+12,&temp,2);

		in=(int)(a4&(char)31);
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*16+14,&temp,2);
	}
}

void g726_32bit_Encode(unsigned char *speech,char *bitstream)
{
	g726_state state_ptr;
	short temp[480];
	int i;

	g726_init_state(&state_ptr);

	memcpy(temp,speech,960);
	
	for(i=0;i<240;i++)
	{
		*(bitstream+i)=(((char)(g726_32_encoder(temp[i*2],AUDIO_ENCODING_LINEAR,&state_ptr)))<<4)|(((char)(g726_32_encoder(temp[i*2+1],AUDIO_ENCODING_LINEAR,&state_ptr))));
	}
}

void g726_32bit_Decode(char *bitstream,unsigned char *speech)
{
	g726_state state_ptr;
	int i;
	int in;
	short temp;

	g726_init_state(&state_ptr);
	
	for(i=0;i<240;i++)
	{
		in=(int)((((*(bitstream+i))&(char)240)>>4)&(char)15);
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*4,&temp,2);
		in=(int)(((*(bitstream+i))&(char)15));
		temp=g726_32_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*4+2,&temp,2);
	}
}

void g726_16bit_Encode(unsigned char *speech,char *bitstream)
{
	g726_state state_ptr;
	short temp[480];
	int i;

	g726_init_state(&state_ptr);
	memcpy(temp,speech,960);

	for(i=0;i<120;i++)
	{
		*(bitstream+i)=(((char)(g726_16_encoder(temp[i*4],AUDIO_ENCODING_LINEAR,&state_ptr)))<<6)|(((char)(g726_16_encoder(temp[i*4+1],AUDIO_ENCODING_LINEAR,&state_ptr)))<<4)|(((char)(g726_16_encoder(temp[i*4+2],AUDIO_ENCODING_LINEAR,&state_ptr)))<<2)|(((char)(g726_16_encoder(temp[i*4+3],AUDIO_ENCODING_LINEAR,&state_ptr))));
	}
}

void g726_16bit_Decode(char *bitstream,unsigned char *speech)
{
	g726_state state_ptr;
	int i;
	int in;
	short temp;

	g726_init_state(&state_ptr);


	for(i=0;i<120;i++)
	{
		in=(int)(((*(bitstream+i))&(char)192)>>6);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8,&temp,2);
		in=(int)(((*(bitstream+i))&(char)48)>>4);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+2,&temp,2);
		in=(int)(((*(bitstream+i))&(char)12)>>2);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+4,&temp,2);
		in=(int)(((*(bitstream+i))&(char)3));
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+6,&temp,2);
	}
}


extern void g726_Encode(unsigned char *speech,char *bitstream,int& streamSize,int bitnum)
{
	switch (bitnum)
	{
	case 16:
		streamSize=120;
		g726_16bit_Encode(speech,bitstream);
		break;
	case 32:
		streamSize=240;
		g726_32bit_Encode(speech,bitstream);
		break;
	case 40:
		streamSize=300;
		g726_40bit_Encode(speech,bitstream);
		break;
	}
}

extern void g726_Decode(char *bitstream,unsigned char *speech,int bitnum)
{
	g726_state state_ptr;
	int i;
	int in;
	short temp;

	g726_init_state(&state_ptr);

	int size = sizeof(short);

	for(i=0;i<120;i++)
	{
		in=(int)(((*(bitstream+i))&(char)192)>>6);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8,(char*)&temp,size);
		in=(int)(((*(bitstream+i))&(char)48)>>4);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+2,(char*)&temp,size);
		in=(int)(((*(bitstream+i))&(char)12)>>2);
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+4,(char*)&temp,size);
		in=(int)(((*(bitstream+i))&(char)3));
		temp=g726_16_decoder(in,AUDIO_ENCODING_LINEAR,&state_ptr);
		memcpy(speech+i*8+6,(char*)&temp,size);
	}
		/*
	switch (bitnum)
	{
	case 16:
		g726_16bit_Decode(bitstream,speech);
		LOGE("here is g726_decoder.............over");
		break;
	case 32:
		g726_32bit_Decode(bitstream,speech);
		break;
	case 40:
		g726_40bit_Decode(bitstream,speech);
		break;
	}
	*/
}

extern void test() {

}
