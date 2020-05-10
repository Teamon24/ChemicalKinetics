package checks;

import com.nir.utils.LorentzStrangeAttractor2;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.method.hardcoded.AdamBashforth5thMethods;
import com.nir.utils.math.solution.Solution;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class AdamBashforthSolutionPlotCheck extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        //Система уравнений
        final LorentzStrangeAttractor2 system = new LorentzStrangeAttractor2();
        final List<String> titles = system.titles();

        //Подготовка объектов для данных с решением системы
        //и открытие графиков
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(titles);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Запуск решения системы уравнений
        final InitialPoint initialPoint = system.initialPoint();
        final ComputationConfigs computationConfigs = new ComputationConfigs(0.00001, 2000000);

        final AdamBashforth5thMethods method = new AdamBashforth5thMethods();

        method.setFirstAccelerationPointMethodName("Runge-Kutta 4-order (Hardcoded)");
        method.setUp(initialPoint, computationConfigs);

        System.out.println(String.format("Numeric Method: \"%s\"", method.getName()));

        final Runnable solution =
            Solution
                .method(method)
                .computation(computationConfigs)
                .system(system)
                .initialData(initialPoint)
                .datasets(dataSets)
                .task();

        PlatformUtils.runLater(solution);
    }


}
