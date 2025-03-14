package ca.fxco.pistonlib.mixin.merging;

import ca.fxco.pistonlib.pistonLogic.accessible.ConfigurablePistonBehavior;
import ca.fxco.pistonlib.pistonLogic.accessible.ConfigurablePistonMerging;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlock.class)
public class SignBlock_mergeMixin implements ConfigurablePistonMerging, ConfigurablePistonBehavior {

    @Override
    public boolean usesConfigurablePistonMerging() {
        return true;
    }

    @Override
    public boolean usesConfigurablePistonBehavior() {
        return true;
    }

    @Override
    public boolean canMerge(BlockState state, BlockState mergingIntoState, Direction dir) {
        if (state.getBlock() != mergingIntoState.getBlock()) {
            return false;
        }
        return state.getValues().equals(mergingIntoState.getValues());
    }
}
