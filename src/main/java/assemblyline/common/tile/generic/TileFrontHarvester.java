package assemblyline.common.tile.generic;

import assemblyline.common.inventory.container.generic.AbstractHarvesterContainer;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class TileFrontHarvester extends TileOutlineArea {

	public static final int DEFAULT_WAIT_TICKS = 600;
	public static final int FASTEST_WAIT_TICKS = 60;
	protected int ticksSinceCheck = 0;
	protected int currentWaitTime;
	public double clientProgress;
	protected double powerUsageMultiplier = 1;
	public double clientUsageMultiplier;

	protected TileFrontHarvester(BlockEntityType<?> type, BlockPos pos, BlockState state, double maxJoules, int voltage, String name) {
		super(type, pos, state);
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler().customPacketWriter(this::createPacket).guiPacketWriter(this::createPacket).customPacketReader(this::readPacket).guiPacketReader(this::readPacket));
		addComponent(new ComponentTickable().tickServer(this::tickServer).tickClient(this::tickClient).tickCommon(this::tickCommon));
		addComponent(new ComponentElectrodynamic(this).relativeInput(getVoltageInput()).voltage(voltage).maxJoules(maxJoules));
		addComponent(getInv(this));
		addComponent(new ComponentContainerProvider("container." + name).createMenu(this::getContainer));
	}

	public abstract void tickServer(ComponentTickable tickable);

	public abstract void tickClient(ComponentTickable tickable);

	public abstract void tickCommon(ComponentTickable tickable);

	@Override
	public AABB getAABB(int width, int length, int height, boolean isFlipped, boolean isClient, TileOutlineArea grinder) {
		ComponentDirection dir = grinder.getComponent(ComponentType.Direction);
		BlockPos machinePos = grinder.getBlockPos();
		BlockPos blockInFront = machinePos.relative(isFlipped ? dir.getDirection().getOpposite() : dir.getDirection());
		BlockPos startPos;
		BlockPos endPos;
		int deltaX = blockInFront.getX() - machinePos.getX();
		int deltaZ = blockInFront.getZ() - machinePos.getZ();
		int xShift;
		int zShift;
		int yShift = height - 1;
		if (isFlipped) {
			// voltage south
			if (deltaX == 0) {
				xShift = deltaZ * (width + 2) / 2;
				zShift = deltaZ * length;
				startPos = new BlockPos(blockInFront.getX() + (isClient && deltaZ < 0 ? xShift + 1 : xShift), blockInFront.getY() + yShift, blockInFront.getZ() + (isClient && deltaZ < 0 ? zShift + 1 : zShift));
				endPos = new BlockPos(blockInFront.getX() - (isClient && deltaZ > 0 ? xShift - 1 : xShift), blockInFront.getY(), blockInFront.getZ() - (isClient && deltaZ > 0 ? deltaZ - 1 : deltaZ));
				return new AABB(startPos, endPos);
			} else if (deltaZ == 0) {
				xShift = deltaX * width;
				zShift = deltaX * (length + 2) / 2;
				startPos = new BlockPos(blockInFront.getX() + (isClient && deltaX < 0 ? xShift + 1 : xShift), blockInFront.getY() + yShift, blockInFront.getZ() + (isClient && deltaX < 0 ? zShift + 1 : zShift));
				endPos = new BlockPos(blockInFront.getX() - (isClient && deltaX > 0 ? 0 : deltaX), blockInFront.getY(), blockInFront.getZ() - (isClient && deltaX > 0 ? zShift - 1 : zShift));
				return new AABB(startPos, endPos);
			}
		} else // voltage north
		// this should work
		if (deltaX == 0) {
			xShift = isClient ? deltaZ * width / 2 : deltaZ * (width + 2) / 2;
			zShift = deltaZ * length;
			startPos = new BlockPos(blockInFront.getX() + xShift + (isClient ? deltaZ : 0), blockInFront.getY() + yShift, blockInFront.getZ() + zShift);
			endPos = new BlockPos(blockInFront.getX() - xShift, blockInFront.getY(), blockInFront.getZ() - (isClient ? 0 : deltaZ));
			return new AABB(startPos, endPos);
		} else if (deltaZ == 0) {
			xShift = deltaX * width;
			zShift = isClient ? deltaX * length / 2 : deltaX * (length + 2) / 2;
			startPos = new BlockPos(blockInFront.getX() + xShift, blockInFront.getY() + yShift, blockInFront.getZ() + zShift + (isClient ? deltaX : 0));
			endPos = new BlockPos(blockInFront.getX() - (isClient ? 0 : deltaX), blockInFront.getY(), blockInFront.getZ() - zShift);
			return new AABB(startPos, endPos);
		}
		return new AABB(0, 0, 0, 0, 0, 0);
	}

	@Override
	protected void createPacket(CompoundTag nbt) {
		super.createPacket(nbt);
		nbt.putDouble("clientProgress", (double) ticksSinceCheck / (double) currentWaitTime);
		nbt.putDouble("clientMultiplier", powerUsageMultiplier);
	}

	@Override
	protected void readPacket(CompoundTag nbt) {
		super.readPacket(nbt);
		clientProgress = nbt.getDouble("clientProgress");
		clientUsageMultiplier = nbt.getDouble("clientMultiplier");
	}

	public abstract double getUsage();

	public abstract ComponentInventory getInv(TileFrontHarvester harvester);

	public abstract AbstractHarvesterContainer getContainer(int id, Inventory player);

	public Direction getVoltageInput() {
		return Direction.SOUTH;
	}

}
