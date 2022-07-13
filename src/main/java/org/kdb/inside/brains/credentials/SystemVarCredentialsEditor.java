package org.kdb.inside.brains.credentials;

import org.kdb.inside.brains.core.credentials.CredentialEditor;
import org.kdb.inside.brains.core.credentials.CredentialsError;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SystemVarCredentialsEditor extends CredentialEditor {
    private final JTextField usernameField = new JTextField();
    private final JComboBox<String> varnameField = new JComboBox<>();

    public SystemVarCredentialsEditor() {
        final JPanel p = new JPanel(new GridBagLayout());

        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.HORIZONTAL;

        addField(p, c, "Username", usernameField);
        addField(p, c, "Environment Variable", varnameField);

        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processCredentialChanged(getCredentials());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                processCredentialChanged(getCredentials());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processCredentialChanged(getCredentials());
            }
        });

        System.getenv().keySet().forEach(varnameField::addItem);
        varnameField.addItemListener(e -> processCredentialChanged(getCredentials()));

        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
    }

    @Override
    public String getCredentials() {
        return SystemVarCredentialsProvider.join(usernameField.getText(), (String) varnameField.getSelectedItem());
    }

    @Override
    public String getViewableCredentials() {
        return getCredentials();
    }

    @Override
    public void setCredentials(String credentials) {
        final String[] split = credentials.split(":");
        if (split.length > 0) {
            usernameField.setText(split[0]);
        }
        if (split.length > 1) {
            varnameField.setSelectedItem(split[1]);
        }
    }

    @Override
    public List<CredentialsError> validateEditor() {
        List<CredentialsError> res = new ArrayList<>();
        if (usernameField.getText().isBlank()) {
            res.add(new CredentialsError("Username can't be empty", usernameField));
        }

        final String selectedItem = (String) varnameField.getSelectedItem();
        if (selectedItem == null) {
            res.add(new CredentialsError("Environment variable not selected", varnameField));
        } else if (!System.getenv().containsKey(selectedItem)) {
            res.add(new CredentialsError("Environment variable '" + selectedItem + "' is unknown", varnameField));
        }
        return res;
    }

    private void addField(JPanel p, GridBagConstraints c, String caption, JComponent comp) {
        c.gridx = 0;
        c.gridy += 1;
        c.weightx = 0;

        final JLabel l = new JLabel(caption);
        l.setLabelFor(comp);
        p.add(l, c);

        c.gridx = 1;
        c.weightx = 1;
        p.add(comp, c);
    }
}