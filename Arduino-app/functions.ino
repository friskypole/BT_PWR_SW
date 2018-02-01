extern unsigned char byte_count;
extern uint16_t ttl;

void load_request(unsigned char data){            
  if(data=='@'){                      //@ symbol means end of data transmission
    UCSR0B &= (0<<7);                 //Disables RX Complete Interrupt
    PORTD &= (0<<2);                  //Turn off power to Bluetooth module
    DDRB |= (1<<2);                   //Set D10 as output
    PORTB = 0;                        //Enable power supply VOLTAGE TO LOAD (via PNP)
    _TIMER_init();                    //Enable Timer
  } 
  
  else{
    data-=48;                         //ASCII to Number
    ttl+=data*pow10(byte_count);      //Android application sends the number in reversed order,
    byte_count++;                     //so we increase the magnitude each time
    _TX(data);
  }
}

int pow10(int byte_count){            //Function which calculates 10^(byte_count)
  int mag=1;
  for (int i; i<=byte_count; i++){
    mag*=10;
  }
  return mag;
}

void _TX(unsigned char info){
  while (!( UCSR0A & (1<<UDRE0))) {}  //Wait for empty transmit buffer
  UDR0 = info;                        //Put data into buffer, sends the data
}
