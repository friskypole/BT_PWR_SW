#include <avr/io.h>
#include <avr/interrupt.h>

unsigned char byte_count=0;           //counting received digits
uint16_t ttl=0;                       //time to live
uint16_t timer=0;                     //stores elapsed time in seconds

ISR(USART_RX_vect){
  unsigned char data=UDR0;            //stores byte transmitted by bluetooth module
  load_request(data);                 //calls function which handles the incoming Bluetooth data
}

ISR(TIMER1_COMPA_vect){               //ISR for timer
  timer +=4;                          //4 seconds passed

  if (timer >= ttl){                  //compare current time with end time
    PORTB |= (1<<2);                  //Turn off VOLATGE TO LOAD
    DDRD = 0;                         //Set digital pins to input(high Z)
    cli();                            //globally disable interrupts
    while(1){ 
      SLEEP_MODE_EXT_STANDBY;         //Deepest sleep available, negligible power usage.
    }
  }
  TCNT1 = 0;                          //reset counter value
}

int main (void)
{
  DDRD |= (1<<2)|(1<<4);              //Set digital pin 2/4 to output
  PORTD &= (0<<4);                    //Set d4 to low (PMOS controlling Arduino power supply)
  PORTD |= (1<<2);                    //Set d2 to high (NMOS controlling Bluetooth module power)
  _CLK_PS();
  _USART_init();
  
  while(1) {
    SLEEP_MODE_IDLE;
  }
}
