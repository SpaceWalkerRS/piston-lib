package ca.fxco.pistonlib.blocks.pistons.configurablePiston;

import ca.fxco.pistonlib.blocks.pistons.speedPiston.SpeedMovingBlockEntity;
import ca.fxco.pistonlib.pistonLogic.families.PistonFamily;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ConfigurableMovingBlockEntity extends SpeedMovingBlockEntity {

    public ConfigurableMovingBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public ConfigurableMovingBlockEntity(PistonFamily family, BlockPos pos, BlockState state, BlockState movedState,
                                         BlockEntity movedBlockEntity, Direction facing, boolean extending,
                                         boolean isSourcePiston) {
        super(family, pos, state, movedState, movedBlockEntity, facing, extending, isSourcePiston);
    }

    @Override
    protected void moveCollidedEntities(float nextProgress) {
        if (this.getFamily().isTranslocation()) {
            VoxelShape vs = this.movedState.getCollisionShape(this.level, this.worldPosition);
            if (vs.isEmpty()) return;
            AABB box = vs.bounds().move(this.worldPosition).inflate(0.01D); // Cheating ;)
            List<Entity> list = this.level.getEntities(null, box);
            if (list.isEmpty()) return;
            boolean isSlime = this.movedState.is(Blocks.SLIME_BLOCK);
            Direction dir = this.getMovementDirection();
            float speed = this.speed();
            for (Entity entity : list) {
                if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                    if (isSlime && this.speed() < 0.1F) { //No player check, just like before
                        Vec3 vec3 = entity.getDeltaMovement();
                        double x = vec3.x, y = vec3.y, z = vec3.z;
                        switch (dir.getAxis()) {
                            case X -> x = dir.getStepX() * speed;
                            case Y -> y = dir.getStepY() * speed;
                            case Z -> z = dir.getStepZ() * speed;
                        }
                        entity.setDeltaMovement(x, y, z);
                    }
                    double x = 0.0D, y = 0.0D, z = 0.0D;
                    AABB entityBox = entity.getBoundingBox();
                    boolean positive = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                    switch (dir.getAxis()) {
                        case X -> x = (positive ? box.maxX - entityBox.minX : entityBox.maxX - box.minX) + 0.1D;
                        case Y -> y = (positive ? box.maxY - entityBox.minY : entityBox.maxY - box.minY) + 0.1D;
                        case Z -> z = (positive ? box.maxZ - entityBox.minZ : entityBox.maxZ - box.minZ) + 0.1D;
                    }
                    entity.move(MoverType.SELF, new Vec3(
                            x * (double) dir.getStepX() * speed,
                            y * (double) dir.getStepY() * speed,
                            z * (double) dir.getStepZ() * speed
                    ));
                }
            }
        } else {
            super.moveCollidedEntities(nextProgress); // Just speed
        }
    }
}
