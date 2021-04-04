package com.heisenberg.logeasy.views.aggregators;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.heisenberg.logeasy.data.dal.AggregatorDAL;
import com.heisenberg.logeasy.data.entity.Aggregator;
//import com.heisenberg.logeasy.data.service.AggregatorService;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.heisenberg.logeasy.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.web.client.RestTemplate;

@Route(value = "aggregators", layout = MainView.class)
@PageTitle("Aggregators")
@CssImport("./views/aggregators/aggregators-view.css")
public class AggregatorsView extends Div {

    private Grid<Aggregator> grid = new Grid<>(Aggregator.class, false);

    private TextField hostId;
    private TextField aggregatorUrl;
    private TextField maxThreads ;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Aggregator> binder;

    private Aggregator aggregator;

    @Value("${logeasy.backend.url}")
    private String backendUrl ;

    private RestTemplate restTemplate ;

    public AggregatorsView(@Autowired AggregatorDAL aggregatorDAL, @Autowired RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build() ;
        addClassName("aggregators-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("aggregatorId").setAutoWidth(true) ;
        grid.addColumn("hostId").setAutoWidth(true);
        grid.addColumn("aggregatorUrl").setAutoWidth(true);
        grid.addColumn("maxThreads").setAutoWidth(true) ;
        grid.setItems(aggregatorDAL.getAll());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<Aggregator> aggregatorFromBackend = Optional.ofNullable(
                        aggregatorDAL.getAggregatorById(event.getValue().getAggregatorId())
                );
                // when a row is selected but the data is no longer available, refresh grid
                if (aggregatorFromBackend.isPresent()) {
                    populateForm(aggregatorFromBackend.get());
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
        binder = new BeanValidationBinder<>(Aggregator.class);
        binder.forField(maxThreads).asRequired().withConverter(new StringToIntegerConverter()).bind(Aggregator::getMaxThreads, Aggregator::setMaxThreads) ;
        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.aggregator == null) {
                    this.aggregator = new Aggregator();
                }
                binder.writeBean(this.aggregator);
                Map<String, Object> requestBody = new HashMap<>() ;
                requestBody.put("hostId", this.aggregator.getHostId()) ;
                requestBody.put("aggregatorUrl", this.aggregator.getAggregatorUrl()) ;
                requestBody.put("maxThreads", this.aggregator.getMaxThreads()) ;

                Map<String, String> response = restTemplate.postForObject(backendUrl+ "/register/aggregator", requestBody, Map.class);
                if(response.get("status").equals("SUCCESS")){
                    this.aggregator.setAggregatorId(response.get("aggregatorId"));
                    aggregatorDAL.saveAggregator(this.aggregator) ;
                    clearForm();
                    refreshGrid();
                    Notification.show("Aggregator details stored.");
                }
                else{
                    Notification.show("Aggregator registration failed : " +  response.get("hostId") + " " + response.get("responseMessage")) ;
                    refreshGrid();
                }
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the aggregator details.");
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
        aggregatorUrl = new TextField("Aggregator Url");
        maxThreads = new TextField("Maximum number of threads") ;
        Component[] fields = new Component[]{hostId, aggregatorUrl, maxThreads};

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

    private void populateForm(Aggregator value) {
        this.aggregator = value;
        binder.readBean(this.aggregator);

    }
}
