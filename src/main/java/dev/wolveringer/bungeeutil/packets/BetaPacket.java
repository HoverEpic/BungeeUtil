package dev.wolveringer.bungeeutil.packets;

import javax.naming.OperationNotSupportedException;

import dev.wolveringer.bungeeutil.DebugProperties;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import io.netty.buffer.ByteBuf;

public abstract class BetaPacket extends Packet{

	public BetaPacket() {}

	public BetaPacket(byte id) {
		super(id);

	}
	public BetaPacket(int i) {
		super(i);

	}
	@Override
	public ByteBuf getByteBuf(ClientVersion version) {
		if(!DebugProperties.PACKET_DEVELOPMENT) {
			throw new RuntimeException(new OperationNotSupportedException("Packet is stille under development!"));
		}
		return super.getByteBuf(version);
	}

	@Override
	public Packet load(ByteBuf b,ClientVersion version) {
		if(!DebugProperties.PACKET_DEVELOPMENT) {
			throw new RuntimeException(new OperationNotSupportedException("Packet is stille under development!"));
		}
		return super.load(b,version);
	}
}
