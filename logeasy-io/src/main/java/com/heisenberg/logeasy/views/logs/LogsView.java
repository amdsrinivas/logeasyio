package com.heisenberg.logeasy.views.logs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.heisenberg.logeasy.data.dal.LoggerDAL;
import com.heisenberg.logeasy.data.entity.Log;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.heisenberg.logeasy.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.web.client.RestTemplate;

@Route(value = "logs", layout = MainView.class)
@PageTitle("Logs")
@CssImport("./views/logs/logs-view.css")
public class LogsView extends Div {

    private Grid<Log> grid = new Grid<>(Log.class, false);

    private TextField hostId;
    private TextField logPath;
    private TextField logSchema ;
    private TextField logFields ;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Log> binder;

    private Log log;

    @Value("${logeasy.backend.url}")
    private String backendUrl ;
    private LoggerDAL loggerDAL ;
    private RestTemplate restTemplate ;

    public LogsView(@Autowired LoggerDAL loggerDAL, @Autowired RestTemplateBuilder builder) {
        this.loggerDAL = loggerDAL ;
        this.restTemplate = builder.build() ;

        addClassName("logs-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        // Configure Grid
        grid.addColumn("logId").setAutoWidth(true) ;
        grid.addColumn("hostId").setAutoWidth(true);
        grid.addColumn("logPath").setAutoWidth(true);
        grid.addColumn("logSchema").setAutoWidth(true);
        grid.addColumn("logFields").setAutoWidth(true) ;
        grid.setItems(loggerDAL.getAll());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Log> logFromBackend = Optional.ofNullable(
                        loggerDAL.getLoggerById(event.getValue().getLogId())
                );
                // when a row is selected but the data is no longer available, refresh grid
                if (logFromBackend.isPresent()) {
                    populateForm(logFromBackend.get());
                    save.setEnabled(false);
                    cancel.setEnabled(false);
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Log.class);
        binder.forField(logSchema).asRequired().withConverter(new StringToStringListConverter()).bind(Log::getLogSchema, Log::setLogSchema) ;
        binder.forField(logFields).asRequired().withConverter(new StringToIntegerStringsConverter()).bind(Log::getLogFields, Log::setLogFields) ;
        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.log == null) {
                    this.log = new Log();
                }
                binder.writeBean(this.log);
                Map<String, Object> requestBody = new HashMap<>() ;
                requestBody.put("hostId", this.log.getHostId()) ;
                requestBody.put("logPath", this.log.getLogPath()) ;
                requestBody.put("logFields", this.log.getLogSchema()) ;
                requestBody.put("ingestedFields", this.log.getLogFields()) ;

                Map<String, String> response = restTemplate.postForObject(backendUrl+ "/register/log", requestBody, Map.class);
                if(response.get("status").equals("SUCCESS")){
                    this.log.setLogId(response.get("logId"));
                    loggerDAL.saveLog(this.log) ;
                    clearForm();
                    refreshGrid();
                    Notification.show("Logger details stored.");
                }
                else{
                    Notification.show("Logger registration failed : " +  response.get("logId") + " " + response.get("responseMessage")) ;
                    refreshGrid();
                }
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the log details.");
            }
        });

    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        hostId = new TextField("Host Id");
        logPath = new TextField("Log Path");
        logSchema = new TextField("Log Schema.\nUse , as separator") ;
        logFields = new TextField("Enter the index of log fields to be ingested.\nUse , as separator") ;
        Component[] fields = new Component[]{hostId, logPath, logSchema, logFields};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getListDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Log value) {
        this.log = value;
        binder.readBean(this.log);

    }
}
