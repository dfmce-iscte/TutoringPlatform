package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import server.Teacher;
import server.Appointment;

public class CombinedGui extends JFrame {

    private Map<String, JTextField> subjectTextFields;
<<<<<<< Updated upstream
=======
   
>>>>>>> Stashed changes

    private Teacher teacher;

    public CombinedGui(Teacher teacher) {
        this.teacher = teacher;
        buildGui();
    }

    private void buildGui() {
        // Set up the GUI components
        JPanel panel = new JPanel();
<<<<<<< Updated upstream
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
=======
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
>>>>>>> Stashed changes
        panel.setLayout(new GridLayout(1, 2));

        // Left Panel (MainGui)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Subjects Panel
        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new GridLayout(0, 2));

        // Add Subject Button
        JButton addButton = new JButton("Add Subject");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = JOptionPane.showInputDialog("Enter subject name:");
                if (subject != null && !subject.isEmpty()
                        && !teacher.getSubjectsWithRates().keySet().contains(subject)) {
                    // Create a text field for the subject
                    JTextField textField = new JTextField();

                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                teacher.addSubjectWithRates(subject, Double.parseDouble(textField.getText()));
                            }
                        }
                    });
                    // Add a label for the subject
                    subjectsPanel.add(new JLabel(subject));
                    // Add the text field for the subject
                    subjectsPanel.add(textField);
                    // Refresh the panel
                    subjectsPanel.revalidate();
                    subjectsPanel.repaint();
                }
            }
        });

        // Delete Subject Button
        // JButton deleteButton = new JButton("Delete Subject");
        // deleteButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // String selectedSubject = JOptionPane.showInputDialog("Enter subject name to
        // delete:");
        // if (selectedSubject != null && !selectedSubject.isEmpty()) {
        // if (teacher.getSubjectsWithRates().keySet().contains(selectedSubject)) {
        // // teacher.removeSubject(selectedSubject);

        // subjectTextFields.remove(selectedSubject);
        // // Refresh the panel
        // subjectsPanel.removeAll();
        // for (String subject : teacher.getSubjectsWithRates().keySet()) {
        // subjectsPanel.add(new JLabel(subject));
        // subjectsPanel.add(subjectTextFields.get(subject));
        // }
        // subjectsPanel.revalidate();
        // subjectsPanel.repaint();
        // }
        // }
        // }
        // });Joana

        leftPanel.add(new JScrollPane(subjectsPanel), BorderLayout.CENTER);
        leftPanel.add(addButton, BorderLayout.NORTH);
        // leftPanel.add(deleteButton, BorderLayout.SOUTH);

        // Right Panel (MainFrame)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Appointments List
        DefaultListModel<Appointment> appointmentsListModel = new DefaultListModel<>();
        JList<Appointment> appointmentsList = new JList<>(appointmentsListModel);

        appointmentsList.setCellRenderer(new AppointmentListCellRenderer());
        JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsList);
        rightPanel.add(appointmentsScrollPane, BorderLayout.CENTER);

        // Create and Delete Appointment Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Create Appointment Button
        JButton createAppointmentButton = new JButton("Create Appointment");
        createAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel appointmentPanel = new JPanel();
                appointmentPanel.setLayout(new GridLayout(5, 2));

                // Subject Dropdown
                JComboBox<String> subjectDropdown = new JComboBox<>(
                        teacher.getSubjectsWithRates().keySet().toArray(new String[0]));
                appointmentPanel.add(new JLabel("Subject:"));
                appointmentPanel.add(subjectDropdown);

                // Day Selector
                appointmentPanel.add(new JLabel("Day:"));
                JSpinner daySpinner = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor dayEditor = new JSpinner.DateEditor(daySpinner, "dd/MM/yyyy");
                daySpinner.setEditor(dayEditor);
                appointmentPanel.add(daySpinner);

                // Start Time Selector
                appointmentPanel.add(new JLabel("Start Time:"));
                JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
                startTimeSpinner.setEditor(startTimeEditor);
                appointmentPanel.add(startTimeSpinner);

                // End Time Selector
                appointmentPanel.add(new JLabel("End Time:"));
                JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
                endTimeSpinner.setEditor(endTimeEditor);
                appointmentPanel.add(endTimeSpinner);

                int result = JOptionPane.showConfirmDialog(null, appointmentPanel, "Create Appointment",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String subject = (String) subjectDropdown.getSelectedItem();

                    // Get the Date from the JSpinner
                    Date day = (Date) daySpinner.getValue();
                    Date startTimeDate = (Date) startTimeSpinner.getValue();
                    Date endTimeDate = (Date) endTimeSpinner.getValue();

                    // Convert the Date to LocalDateTime
                    LocalDateTime startTime = startTimeDate.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    LocalDateTime endTime = endTimeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    LocalDateTime initial_time = LocalDateTime.ofInstant(day.toInstant(), ZoneId.systemDefault())
                            .withHour(startTime.getHour()).withMinute(startTime.getMinute());
                    LocalDateTime final_time = LocalDateTime.ofInstant(day.toInstant(), ZoneId.systemDefault())
                            .withHour(endTime.getHour()).withMinute(endTime.getMinute());

                    Appointment new_appointment;
                    try {
                        new_appointment = teacher.createAppointment(initial_time, final_time, subject);
                        if (new_appointment == null) {
                            JOptionPane.showMessageDialog(null, "SLOT NOT AVAILABLE", "ERROR",
                                    JOptionPane.ERROR_MESSAGE);

                        } else {
                            appointmentsListModel.addElement(new_appointment);
                        }

                        // hasStudent = true;
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonsPanel.add(createAppointmentButton);

        // // Delete Appointment Button
        // JButton deleteAppointmentButton = new JButton("Delete Appointment");
        // deleteAppointmentButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // Appointment selectedAppointment = appointmentsList.getSelectedValue();
        // if (selectedAppointment != null) {
        // // VER ESTA PARTE MELHOR
        // // appointments.remove(selectedAppointment);
        // // appointmentsListModel.removeElement(selectedAppointment);
        // }
        // }
        // });
        // buttonsPanel.add(deleteAppointmentButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        panel.add(leftPanel);
        panel.add(rightPanel);

        // Set up the JFrame
        setTitle("Teacher: " + teacher.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setContentPane(panel);
        setVisible(true);
    }


    static class AppointmentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (renderer instanceof JLabel && value instanceof Appointment) {
                Appointment appointment = (Appointment) value;
                try {
                    ((JLabel) renderer).setText(appointment.to_string());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return renderer;
        }
    }
}
