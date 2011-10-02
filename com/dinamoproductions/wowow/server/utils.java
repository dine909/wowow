package com.dinamoproductions.wowow.server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Scanner;

public class Utils {
	public final static class ChannelTools {
		public static void fastChannelCopy(final ReadableByteChannel src,
				final WritableByteChannel dest) throws IOException {
			final ByteBuffer buffer = ByteBuffer.allocateDirect(8 * 1024);
			while (src.read(buffer) != -1) {
				// prepare the buffer to be drained
				buffer.flip();
				// write to the channel, may block
				dest.write(buffer);
				// If partial transfer, shift remainder down
				// If buffer is empty, same as doing clear()
				buffer.compact();
			}
			// EOF will leave buffer in fill state
			buffer.flip();
			// make sure the buffer is fully drained.
			while (buffer.hasRemaining()) {
				dest.write(buffer);
			}
		}

		public static void streamCopy(final InputStream is, OutputStream os)
				throws IOException {
			fastChannelCopy((ReadableByteChannel) Channels.newChannel(is),
					(WritableByteChannel) Channels.newChannel(os));
		}

		public static String convertStreamToString(InputStream is) {
			return new Scanner(is).useDelimiter("\\A").next();
		}
	}
}
