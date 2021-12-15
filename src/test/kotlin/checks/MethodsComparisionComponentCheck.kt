package checks

import com.nir.ChemicalReaction
import com.nir.beans.method.Methods
import com.nir.beans.MethodsComparisionComponent
import com.nir.beans.reaction.ChemicalReactionComponent
import com.nir.beans.reaction.ChemicalReactionComponent.getCompounds
import com.nir.beans.method.hardcoded.AdamBashforth3thMethods
import com.nir.beans.method.hardcoded.AdamBashforth4thMethods
import com.nir.beans.method.hardcoded.AdamBashforth5thMethods
import com.nir.beans.method.hardcoded.RungeKutta

fun main() {
    val chemicalReaction = ChemicalReaction.chemicalReaction3()
    val reactionStages = chemicalReaction.reactionStages
    val compounds = getCompounds(reactionStages)
    val initialPoint = chemicalReaction.initialPoint
    val computationConfigs = chemicalReaction.computationConfigs
    val system = ChemicalReactionComponent.getSystem(chemicalReaction)
    val rungeKutta4General = Methods.getByName("Runge-Kutta 4th-order: v.1 (Generalized)")
    val rungeKutta5General = Methods.getByName("Runge-Kutta 5th-order method (Generalized)")
    val eulerGeneral = Methods.getByName("Forward Euler (Generalized)")
    val rungeKutta4 = RungeKutta(4)
    val rungeKutta5 = RungeKutta(5)
    val euler = Methods.getByName("Forward Euler (Hardcoded)")

    val adamBashforth3thMethods = AdamBashforth3thMethods()
    adamBashforth3thMethods.setFirstAccelerationPointMethod(rungeKutta4)

    val adamBashforth4thMethods = AdamBashforth4thMethods()
    adamBashforth4thMethods.setFirstAccelerationPointMethod(rungeKutta4)

    val adamBashforth5thMethods = AdamBashforth5thMethods()
    adamBashforth5thMethods.setFirstAccelerationPointMethod(rungeKutta4)

    val methods = listOf(
            euler,
            rungeKutta4General,
            rungeKutta4,
            rungeKutta5General,
            rungeKutta5,
            adamBashforth3thMethods,
            adamBashforth4thMethods,
            adamBashforth5thMethods)

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