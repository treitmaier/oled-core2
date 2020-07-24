package xyz.reitmaier.oled2.transport;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.i2c.I2C;

import java.io.IOException;


/**
 * A {@link Transport} implementation that utilises I<sup>2</sup>C.
 *
 * @author fauxpark
 * @author treitmaier
 */
public class I2CTransport implements Transport {
	/**
	 * The Data/Command bit position.
	 */
	private static final int DC_BIT = 6;

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	private DigitalOutput rstPin;

	/**
	 * The internal I<sup>2</sup>C device.
	 */
	private I2C i2c;

	/**
	 * I2CTransport constructor.
	 *
	 * @param rstPin The Digital Output GPIO pin to use for the RST line.
	 * @param i2c The I<sup>2</sup>C device of the display
	 */
	public I2CTransport(DigitalOutput rstPin, I2C i2c) {
		this.rstPin = rstPin;
		this.i2c = i2c;
	}

	@Override
	public void reset() {
		try {
			rstPin.high();
			Thread.sleep(1);
			rstPin.low();
			Thread.sleep(10);
			rstPin.high();
		} catch(InterruptedException | com.pi4j.io.exception.IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		try {
			i2c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void command(int command, int... params) {
		byte[] commandBytes = new byte[params.length + 2];
		commandBytes[0] = (byte) (0 << DC_BIT);
		commandBytes[1] = (byte) command;

		for(int i = 0; i < params.length; i++) {
			commandBytes[i + 2] = (byte) params[i];
		}

		try {
			i2c.write(commandBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void data(byte[] data) {
		byte[] dataBytes = new byte[data.length + 1];
		dataBytes[0] = (byte) (1 << DC_BIT);

		for(int i = 0; i < data.length; i++) {
			dataBytes[i + 1] = data[i];
		}

		try {
			i2c.write(dataBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
