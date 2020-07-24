package xyz.reitmaier.oled2.transport;



import java.io.IOException;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.spi.Spi;

/**
 * A {@link Transport} implementation that utilises SPI.
 *
 * @author fauxpark
 * @author treitmaier
 */
public class SPITransport implements Transport {

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	private DigitalOutput rstPin;

	/**
	 * The GPIO pin corresponding to the D/C line on the display.
	 */
	private DigitalOutput dcPin;

	/**
	 * The internal SPI device.
	 */
	private Spi spi;

	/**
	 * SPITransport constructor.
	 *
	 * @param spi The SPI Device
	 * @param rstPin The Digital Output GPIO pin to use for the RST line.
	 * @param dcPin The Digital Output GPIO pin to use for the D/C line.
	 */
	public SPITransport(Spi spi, DigitalOutput rstPin, DigitalOutput dcPin) {
	    this.spi = spi;
		this.rstPin = rstPin;
		this.dcPin = dcPin;
	}

	@Override
	public void reset() {
		try {
			rstPin.high();
			Thread.sleep(1);
			rstPin.low();
			Thread.sleep(10);
			rstPin.high();
		} catch(com.pi4j.io.exception.IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		try {
			spi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void command(int command, int... params) {
		try {
			dcPin.low();
		} catch (com.pi4j.io.exception.IOException e) {
			e.printStackTrace();
		}

		try {
			spi.write((byte) command);
		} catch(IOException e) {
			e.printStackTrace();
		}

		for(int param : params) {
			try {
				spi.write((byte) param);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void data(byte[] data) {
		try {
			dcPin.high();
		} catch (com.pi4j.io.exception.IOException e) {
			e.printStackTrace();
		}

		try {
			spi.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
