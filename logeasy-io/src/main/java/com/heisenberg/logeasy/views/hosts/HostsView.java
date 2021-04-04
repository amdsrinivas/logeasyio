package com.heisenberg.logeasy.views.hosts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.heisenberg.logeasy.data.dal.HostDAL;
import com.heisenberg.logeasy.data.entity.Host;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.heisenberg.logeasy.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.vaadin.haijian.Exporter;

@Route(value = "hosts", layout = MainView.class)
@PageTitle("Hosts")
@CssImport("./views/hosts/hosts-view.css")
public class HostsView extends Div {
    private final HostDAL hostDAL ;
    private Grid<Host> grid = new Grid<>(Host.class, false);

    private TextField hostId;
    private TextField hostName;
    private TextField hostAddress;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Host> binder;

    private Host host;

    @Value("${logeasy.backend.url}")
    private String backendUrl ;

    private RestTemplate restTemplate ;

    public HostsView(@Autowired HostDAL hostDAL, @Autowired RestTemplateBuilder builder) {
        this.hostDAL = hostDAL ;
        this.restTemplate = builder.build() ;
        addClassName("hosts-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);


        // Configure Grid
        grid.addColumn("hostId").setAutoWidth(true);
        grid.addColumn("hostName").setAutoWidth(true);
        grid.addColumn("hostAddress").setAutoWidth(true);
        grid.setItems(hostDAL.getAll());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            save.setEnabled(true);
            cancel.setEnabled(true);
            if (event.getValue() != null) {
                Optional<Host> hostFromBackend = Optional.ofNullable(
                        hostDAL.getHostById(event.getValue().getHostId())
                );
                // when a row is selected but the data is no longer available, refresh grid
                if (hostFromBackend.isPresent()) {
                    populateForm(hostFromBackend.get());
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
        binder = new BeanValidationBinder<>(Host.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.host == null) {
                    this.host = new Host();
                }
                binder.writeBean(this.host);
                Map<String, String> requestBody = new HashMap<>() ;
                requestBody.put("hostName", this.host.getHostName()) ;
                requestBody.put("hostAddress", this.host.getHostAddress()) ;

                Map<String, String> response = restTemplate.postForObject(backendUrl+ "/register/host", requestBody, Map.class);
                if(response.get("status").equals("SUCCESS")){
                    this.host.setHostId(response.get("hostId"));
                    hostDAL.saveHost(this.host) ;
                    clearForm();
                    refreshGrid();
                    Notification.show("Host details stored.");
                }
                else{
                    Notification.show("Host registration failed : " +  response.get("hostId") + " " + response.get("responseMessage")) ;
                    refreshGrid();
                }
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the host details.");
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
        //hostId = new TextField("Host Id");
        hostName = new TextField("Host Name");
        hostAddress = new TextField("Host Address");
        Component[] fields = new Component[]{hostName, hostAddress};

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
        wrapper.add(grid) ;
    }
    private void updateGridItem(){
        grid.setItems(hostDAL.getAll()) ;
    }
    private void refreshGrid() {
        grid.select(null);
        updateGridItem();
        grid.getListDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Host value) {
        this.host = value;
        binder.readBean(this.host);

    }
}
