package com.gazpromtrans.trainingtasknew.screen.contract;

import com.gazpromtrans.trainingtasknew.entity.*;
import com.gazpromtrans.trainingtasknew.screen.invoice.InvoiceEdit;
import com.gazpromtrans.trainingtasknew.screen.servicecompletioncertificate.ServiceCompletionCertificateEdit;
import io.jmix.bpm.data.form.FormParam;
import io.jmix.bpmui.processform.ProcessFormContext;
import io.jmix.bpmui.processform.annotation.*;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.FileRef;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.Action;
import io.jmix.ui.app.inputdialog.DialogActions;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.app.inputdialog.InputParameter;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.options.ContainerOptions;
import io.jmix.ui.component.data.value.ContainerValueSource;
import io.jmix.ui.component.inputdialog.InputDialogAction;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.*;
import io.jmix.ui.screen.*;
import io.jmix.ui.upload.TemporaryStorage;
import io.jmix.ui.util.OperationResult;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@ProcessForm(
        outcomes = {
                @Outcome(id = "changeState"),
                @Outcome(id = "stateCancel"),
            },
        params = {
                @Param(name = "item"),
                @Param(name = "state"),
                @Param(name = "userList")
            }
)
@UiController("Contract.edit")
@UiDescriptor("contract-edit.xml")
@EditedEntityContainer("contractDc")
public class ContractEdit extends StandardEditor<Contract> {
    @Autowired
    private CollectionPropertyContainer<Stage> stageDc;
    @Autowired
    private TextField<Double> amountField;
    @Autowired
    private TextField<Double> vatField;
    @Autowired
    private TextField<Double> totalAmountField;
    @Autowired
    private EntityPicker<State> stateField;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private FileMultiUploadField filesField;
    @Autowired
    private Notifications notifications;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private DataComponents dataComponents;
    @Autowired
    private Downloader downloader;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private ProcessFormContext processFormContext;
    @ProcessFormParam(name = "item")
    protected Contract contract;
    @ProcessFormParam(name = "state")
    protected String state;
    @ProcessFormParam(name = "userList")
    protected List<User> userList;

   // @Autowired
    //@ProcessVariable(name = "userList")
    //protected List<User> userList;
    @Autowired
    private Action changeState;
    @Autowired
    private HBoxLayout editActions;
    @Autowired
    private Button unLoad;
    @Autowired
    private Button stateCancel;
    @Autowired
    private Table<Stage> stageTable;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe("unLoadFiles")
    public void onUnLoadFiles(Action.ActionPerformedEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Выгрузка файлов")
                .withParameters(InputParameter.entityParameter("file", FileDescriptor.class)
                        .withField(() -> {
                            DataContext dataContext = dataComponents.createDataContext();
                            getScreenData().setDataContext(dataContext);
                            CollectionContainer<FileDescriptor> fileDc = dataComponents.createCollectionContainer(FileDescriptor.class);

                            CollectionLoader<FileDescriptor> fileDl = dataComponents.createCollectionLoader();
                            fileDl.setContainer(fileDc);
                            fileDl.setDataContext(dataContext);
                            fileDl.setQuery("select e from file_Descriptor e where e.contract = :contract");
                            fileDl.setFetchPlan(FetchPlan.BASE);
                            fileDl.setParameter("contract", getEditedEntity());
                            fileDl.load();

                            EntityComboBox<FileDescriptor> comboBox = uiComponents.create(EntityComboBox.NAME);
                            comboBox.setOptions(new ContainerOptions<>(fileDc));

                            comboBox.setCaption("Наименование файла");
                            comboBox.setRequired(true);
                            return comboBox;
                        })
                )
                .withValidator(e -> {
                    FileDescriptor fileDescriptor = e.getValue("file");
                    if (fileDescriptor == null) {
                        return ValidationErrors.of("Файл не выбран!");
                    }
                    return ValidationErrors.none();
                })
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(e -> {
                    if (e.closedWith(DialogOutcome.OK)) {
                        FileDescriptor fileDescriptor = e.getValue("file");
                        if (fileDescriptor != null) {
                            downloader.download(fileDescriptor.getFile());
                        }
                    }
                })
                .show();
    }

    @Subscribe
    public void onInit(InitEvent event) {
        if (contract != null) {
            setEntityToEdit(contract);
            changeState.setCaption(state);
            editActions.setVisible(false);
            unLoad.setVisible(false);
            filesField.setVisible(false);
        } else {
            changeState.setVisible(false);
            stateCancel.setVisible(false);
        }
        filesField.addQueueUploadCompleteListener(
                queueUploadCompleteEvent -> {
                        for (Map.Entry<UUID, String> entry : filesField.getUploadsMap().entrySet()) {
                            UUID fileId = entry.getKey();
                            String fileName = entry.getValue();
                            FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);
                            FileDescriptor fileDescriptor = getScreenData().getDataContext().create(FileDescriptor.class);
                            fileDescriptor.setName(entry.getValue());
                            fileDescriptor.setFile(fileRef);
                            fileDescriptor.setContract(getEditedEntity());
                            getScreenData().getDataContext().commit();
                        }
                    notifications.create()
                            .withCaption("Загруженные файлы: " + filesField.getUploadsMap().values())
                            .show();
                    filesField.clearUploads();
                }
        );
    }

    @Subscribe("stateCancel")
    public void onStateCancel(Action.ActionPerformedEvent event) {
        State st = dataManager.load(State.class).condition(PropertyCondition.equal("name", "Отеменён")).optional().orElse(null);
        getEditedEntity().setState(st);
        closeWithDefaultAction();
//        processFormContext.taskCompletion()
//                .withOutcome("stateCancel")
//                .saveInjectedProcessVariables()
//                .complete();
//        closeWithDefaultAction();
    }

    @Subscribe("changeState")
    public void onChangeState(Action.ActionPerformedEvent event) {
        State st = dataManager.load(State.class).condition(PropertyCondition.equal("name", state)).optional().orElse(null);
        getEditedEntity().setState(st);
        closeWithDefaultAction();
//        OperationResult r = closeWithDefaultAction();
//        r.then(() -> {
//            processFormContext.taskCompletion()
//                    .withOutcome("changeState")
//                    .saveInjectedProcessVariables()
//                    .complete();
//        });
    }

    @Subscribe
    public void onAfterCommitChanges(AfterCommitChangesEvent event) {
        if (state != null) {
            User user = dataManager.load(User.class)
                    .query("select u from User u where u.username = :username")
                    .parameter("username", currentAuthentication.getUser().getUsername())
                    .one();
//            Map<String, Object> params = processFormContext.getTask().getProcessVariables();
//            List<User> userList = (List<User>) params.get("userList");
//            userList.add(user);
            userList.add(user);
            State st = dataManager.load(State.class).condition(PropertyCondition.equal("name", state)).optional().orElse(null);
            if (getEditedEntity().getState().equals(st)) {
                processFormContext.taskCompletion()
                        .withOutcome("changeState")
                        .addProcessVariable("userList", userList)
                        .complete();
            }
            State stCancel = dataManager.load(State.class).condition(PropertyCondition.equal("name", "Отеменён")).optional().orElse(null);
            if (getEditedEntity().getState().equals(stCancel)) {
                processFormContext.taskCompletion()
                        .withOutcome("stateCancel")
                        .addProcessVariable("userList", userList)
                        .complete();
            }
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (getEditedEntity().getStage() == null) {
            Stage stage = getScreenData().getDataContext().create(Stage.class);
            stage.setContract(getEditedEntity());
            stage.setName("1 этап");
            stage.setDateFrom(getEditedEntity().getDateFrom());
            stage.setDateTo(getEditedEntity().getDateTo());
            stage.setAmount(getEditedEntity().getAmount());
            List<Stage> stageList = new ArrayList<>();
            stageList.add(stage);
            getEditedEntity().setStage(stageList);
        }
    }

    @Subscribe
    public void onInitEntity(InitEntityEvent<Contract> event) {
        State state = dataManager.load(State.class).condition(PropertyCondition.equal("code", 0)).optional().orElse(null);
        if (state != null) {
            event.getEntity().setState(state);
        } else {
            closeWithDefaultAction();
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent afterShowEvent) {
        stageDc.addCollectionChangeListener(event -> {
            amountField.setValue(event.getSource().getItems()
                    .stream()
                    .filter(e -> e.getAmount() != null)
                    .mapToDouble(e -> e.getAmount())
                    .sum());
            vatField.setValue(event.getSource().getItems()
                    .stream()
                    .filter(e ->  e.getVat() != null)
                    .mapToDouble(e -> e.getVat())
                    .filter(e -> e != 0.0).sum());
            totalAmountField.setValue(event.getSource().getItems()
                    .stream()
                    .filter(e -> e.getTotalAmount() != null)
                    .mapToDouble(e -> e.getTotalAmount())
                    .sum());
        });
    }

    @Subscribe("stageTable.createInvoice")
    public void onStageTableCreateInvoice(Action.ActionPerformedEvent event) {
        if (stageTable.getSingleSelected().getInvoice() != null) {
            StandardEditor<Invoice> editor = screenBuilders.editor(Invoice.class, this)
                    .editEntity(stageTable.getSingleSelected().getInvoice())
                    .withParentDataContext(getScreenData().getDataContext())
                    .withOpenMode(OpenMode.DIALOG)
                    .withScreenClass(InvoiceEdit.class)
                    .build();
            editor.show();
        } else {
            StandardEditor<Invoice> editor = screenBuilders.editor(Invoice.class, this)
                    .newEntity()
                    .withInitializer(invoice -> {
                        invoice.setAmount(stageTable.getSingleSelected().getAmount());
                        invoice.setVat(stageTable.getSingleSelected().getVat());
                        invoice.setTotalAmount(stageTable.getSingleSelected().getTotalAmount());
                        invoice.setDescription(stageTable.getSingleSelected().getDescription());
                        invoice.setStage(stageTable.getSingleSelected());
                    })
                    .withOpenMode(OpenMode.DIALOG)
                    .withParentDataContext(getScreenData().getDataContext())
                    .withScreenClass(InvoiceEdit.class)
                    .build();
            editor.addAfterCloseListener(closeEvent -> {
                if (closeEvent.closedWith(StandardOutcome.COMMIT)) {
                    stageTable.getSingleSelected().setInvoice(editor.getEditedEntity());
                }
            });
            editor.show();
        }
    }

    @Subscribe("stageTable.createCertificate")
    public void onStageTableCreateCertificate(Action.ActionPerformedEvent event) {
        if (stageTable.getSingleSelected().getServiceCompletionCertificate() != null) {
            StandardEditor<ServiceCompletionCertificate> editor = screenBuilders.editor(ServiceCompletionCertificate.class, this)
                    .editEntity(stageTable.getSingleSelected().getServiceCompletionCertificate())
                    .withParentDataContext(getScreenData().getDataContext())
                    .withOpenMode(OpenMode.DIALOG)
                    .withScreenClass(ServiceCompletionCertificateEdit.class)
                    .build();
            editor.show();
        } else {
            StandardEditor<ServiceCompletionCertificate> editor = screenBuilders.editor(ServiceCompletionCertificate.class, this)
                    .newEntity()
                    .withInitializer(certificate -> {
                        certificate.setAmount(stageTable.getSingleSelected().getAmount());
                        certificate.setVat(stageTable.getSingleSelected().getVat());
                        certificate.setTotalAmount(stageTable.getSingleSelected().getTotalAmount());
                        certificate.setDescription(stageTable.getSingleSelected().getDescription());
                        certificate.setStage(stageTable.getSingleSelected());
                    })
                    .withOpenMode(OpenMode.DIALOG)
                    .withParentDataContext(getScreenData().getDataContext())
                    .withScreenClass(ServiceCompletionCertificateEdit.class)
                    .build();
            editor.addAfterCloseListener(closeEvent -> {
                if (closeEvent.closedWith(StandardOutcome.COMMIT)) {
                    stageTable.getSingleSelected().setServiceCompletionCertificate(editor.getEditedEntity());
                }
            });
            editor.show();
        }
    }
    
}