package checks

import com.nir.ChemicalReaction
import com.nir.beans.ChemicalReactionComponent
import com.nir.beans.ChemicalReactionComponent.getCompounds
import com.nir.beans.Methods
import com.nir.beans.MethodsComparisionComponent
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.hardcoded.AdamBashforth4thMethods
import com.nir.utils.math.method.hardcoded.RungeKutta

fun main() {
    val chemicalReaction = ChemicalReaction.chemicalReaction3()
    val reactionStages = chemicalReaction.reactionStages
    val compounds = getCompounds(reactionStages)
    val initialPoint = chemicalReaction.initialPoint
    val computationConfigs = ComputationConfigs(0.1, 100)
    val system = ChemicalReactionComponent.getSystem(chemicalReaction)

    val rungeKutta4 = RungeKutta(4)
    val rungeKutta5 = RungeKutta(5)
    val euler = Methods.getByName("Forward Euler (Hardcoded)")
    val adamBashforth4thMethods = AdamBashforth4thMethods()
    adamBashforth4thMethods.setFirstAccelerationPointMethod(rungeKutta4)

    val methods = listOf(euler, rungeKutta4, rungeKutta5)

    val comparisions = MethodsComparisionComponent()
            .compare(
                    compounds,
                    system,
                    initialPoint,
                    computationConfigs,
                    methods,
                    chemicalReaction.analyticalSolution
            )
    println()
}