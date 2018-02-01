void _CLK_PS(void){
  CLKPR = 128;                        //set CLKPCE to 1 and other bits to 0
  CLKPR = 0b0100;                     //set CLKPCE to 0 and CLKPS to 0100 - clock prescaler set to 16, frequency is now at 16MHz/16=1MHz
}

void _USART_init(void){
                                      //Setting up the desired baud rate:
  UCSR0A = 0b00100010;                //Sets U2X0, reducing baud rate divisor from 16 to 8, effectively doubling transmission rate
  UBRR0L = 12;                        //Controls the USART Baud Rate, don't need to write to UBRR0H since we want 12
                                      //BAUD=fosc/(8(UBRR+1))=9600; with fosc=1MHz our error=0.2% (in acceptable range)

  UCSR0C = (3<<UCSZ00);               //Set Asynchronous mode for USART, 8bit transmission
  UCSR0B = (1<<RXEN0)|(1<<TXEN0);     //Receiver/Transmitter enable
  UCSR0B |= (1<<7);                   //Enables RX Complete Interrupt

  sei();                              //globally enable interrupts
}

void _TIMER_init(void){
  
  TCCR1B |= 0b011;                    //set timer clock prescaler to 64, our timer clock freq is now 1MHz/64=15.625kHz 
  OCR1A = 62500;                      //setting output compare A to 62500, which will be achieved after 62500/15625=4 seconds
  TIMSK1 |= 0b10;                     //set OCIEA to enable interrupt when OCFA flag is set
  TCNT1 = 0;                          //initialize counter
  
}
