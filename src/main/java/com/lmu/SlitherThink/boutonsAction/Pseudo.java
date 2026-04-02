package com.lmu.SlitherThink.boutonsAction;

import com.lmu.SlitherThink.save.LoadSave;
import com.lmu.SlitherThink.save.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Side;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class Pseudo extends ChangementFenetre {
    @FXML private TextField txtPseudo;
    @FXML private Button btnConfirmer;

    public static String nomJoueur = null; 

    private ContextMenu suggestionsMenu = new ContextMenu();
    private List<String> historiquePseudos;

    @FXML
    public void initialize() {
        SaveManager sm = new SaveManager(LoadSave.getInstance(""));
        this.historiquePseudos = sm.getTousLesPseudos();

        // Désactive le bouton si le champ est vide
        btnConfirmer.disableProperty().bind(txtPseudo.textProperty().isEmpty());

        txtPseudo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 20) {
                txtPseudo.setText(oldVal);
                return;
            }
            autoCompletion(newVal);
        });
        
        txtPseudo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsMenu.hide();
        });
    }

    private void autoCompletion(String saisi) {
        suggestionsMenu.getItems().clear();
        if (saisi.isEmpty() || historiquePseudos == null || historiquePseudos.isEmpty()) {
            suggestionsMenu.hide();
            return;
        }

        // Filtre les pseudos qui commencent par la saisie actuelle
        List<MenuItem> items = historiquePseudos.stream()
            .filter(p -> p.toLowerCase().startsWith(saisi.toLowerCase()))
            .map(p -> {
                MenuItem item = new MenuItem(p);
                item.setOnAction(e -> {
                    txtPseudo.setText(p);
                    txtPseudo.positionCaret(p.length());
                    suggestionsMenu.hide();
                });
                return item;
            })
            .collect(Collectors.toList());

        if (items.isEmpty()) {
            suggestionsMenu.hide();
        } else {
            suggestionsMenu.getItems().addAll(items);
            if (!suggestionsMenu.isShowing()) {
                suggestionsMenu.show(txtPseudo, Side.BOTTOM, 0, 0);
            }
        }
    }

    @FXML
    private void confirmer(ActionEvent event) {
        String pseudo = txtPseudo.getText().trim();
        if (!pseudo.isEmpty()) {
            Pseudo.nomJoueur = pseudo;
            changerFenetre(event, "menuAccueil");
        }
    }
}