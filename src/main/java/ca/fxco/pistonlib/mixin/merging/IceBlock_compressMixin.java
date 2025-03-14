package ca.fxco.pistonlib.mixin.merging;

import ca.fxco.pistonlib.blocks.pistons.mergePiston.MergeBlockEntity;
import ca.fxco.pistonlib.pistonLogic.accessible.ConfigurablePistonMerging;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(IceBlock.class)
public class IceBlock_compressMixin implements ConfigurablePistonMerging {

    @Override
    public boolean usesConfigurablePistonMerging() {
        return true;
    }

    @Override
    public boolean canMultiMerge() {
        return true;
    }

    @Override
    public boolean canMerge(BlockState state, BlockState mergingIntoState, Direction dir) {
        return state.getBlock() == mergingIntoState.getBlock();
    }

    @Override
    public boolean canMultiMerge(BlockState state, BlockState mergingIntoState, Direction dir, Map<Direction, MergeBlockEntity.MergeData> currentlyMerging) {
        return currentlyMerging.size() <= 2; // max 3
    }

    @Override
    public BlockState doMerge(BlockState state, BlockState mergingIntoState, Direction dir) {
        return Blocks.ICE.defaultBlockState();
    }

    @Override
    public BlockState doMultiMerge(Map<Direction, BlockState> states, BlockState mergingIntoState) {
        if (states.size() != 3) {
            return Blocks.ICE.defaultBlockState();
        }
        for (BlockState state : states.values()) {
            if (state.getBlock() != mergingIntoState.getBlock()) {
                return Blocks.ICE.defaultBlockState();
            }
        }
        return Blocks.PACKED_ICE.defaultBlockState();
    }
}
