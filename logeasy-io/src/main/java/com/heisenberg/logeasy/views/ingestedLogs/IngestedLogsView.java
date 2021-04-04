package com.heisenberg.logeasy.views.ingestedLogs;

import com.heisenberg.logeasy.data.dal.LoggerDAL;
import com.heisenberg.logeasy.data.entity.Log;
import com.heisenberg.logeasy.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "data", layout = MainView.class)
@PageTitle("Ingested Logs")
@CssImport("./views/logs/logs-view.css")
public class IngestedLogsView extends Div {
    private Grid<HashMap<String,String>> grid = new Grid<>();
    private LoggerDAL loggerDAL ;
    private Select<String> loggerSelect = new Select<>() ;

    public IngestedLogsView(@Autowired LoggerDAL loggerDAL) {
        this.loggerDAL = loggerDAL ;
        addClassName("logs-view");

        VerticalLayout verticalLayout = new VerticalLayout() ;
        verticalLayout.setSizeFull();

        createSelectLayout(verticalLayout);
        createGridLayout(verticalLayout);

        add(verticalLayout);

        loggerSelect.setLabel("Select a Logger");
        loggerSelect.setItems(this.loggerDAL.getAllLoggers()) ;
        loggerSelect.setPlaceholder("Select a logger from the list");
        loggerSelect.addValueChangeListener(
                event -> {
                    System.out.println("Selected : " + event.getValue());
                    populateGrid(event.getValue());
                }
        ) ;

    }

    private void createGridLayout(VerticalLayout layout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        layout.add(wrapper);
        wrapper.add(grid);
    }

    private void createSelectLayout(VerticalLayout layout){
        Div wrapper = new Div();
        wrapper.setId("select-wrapper");
        layout.add(wrapper);
        wrapper.add(loggerSelect);
    }

    private void populateGrid(String loggerPath){
        grid.removeAllColumns();

        List<HashMap<String, String>> gridRows = this.loggerDAL.getLoggerDataByPath(loggerPath).getLogsList() ;
        Log log = this.loggerDAL.getLoggerByPath(loggerPath) ;
        List<String> columnHeaders = log.getLogSchema().
                stream().
                filter(columnName -> log.getLogFields().contains(log.getLogSchema().indexOf(columnName))).
                collect(Collectors.toList()) ;
        columnHeaders.add("sourceLogFile") ;
        columnHeaders.forEach(columnHeader -> grid.addColumn(stringStringHashMap -> stringStringHashMap.get(columnHeader)));
        List gridColumns = grid.getColumns() ;
        grid.getColumns().forEach(col -> col.setHeader(columnHeaders.get(gridColumns.indexOf(col)))) ;
        grid.setItems(gridRows);

    }
}
