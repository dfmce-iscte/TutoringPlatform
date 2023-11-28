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
    // private List<String> subjects;
    // private List<Double> rates;
    private Map<String, JTextField> subjectTextFields;
    // private List<Appointment> appointments;
    // private String[] appointmentSubjects;
    // private boolean hasStudent;

    private Teacher teacher;

    public CombinedGui(Teacher teacher) {
        this.teacher = teacher;
        buildGui();
    }

    private void buildGui() {
        // Set up the GUI components
        JPanel panel = new JPanel();
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
                if (subject != null && !subject.isEmpty()) {
                    // Create a text field for the subject
                    JTextField textField = new JTextField();

                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                teacher.addSubjectWithRates(subject,Double.parseDouble(textField.getText()));   
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
        JButton deleteButton = new JButton("Delete Subject");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSubject = JOptionPane.showInputDialog("Enter subject name to delete:");
                if (selectedSubject != null && !selectedSubject.isEmpty()) {
                    if (teacher.getSubjectsWithRates().keySet().contains(selectedSubject)) {
                        teacher.removeSubject(selectedSubject);
                        
                        subjectTextFields.remove(selectedSubject);
                        // Refresh the panel
                        subjectsPanel.removeAll();
                        for (String subject : teacher.getSubjectsWithRates().keySet()) {
                            subjectsPanel.add(new JLabel(subject));
                            subjectsPanel.add(subjectTextFields.get(subject));
                        }
                        subjectsPanel.revalidate();
                        subjectsPanel.repaint();
                    }
                }
            }
        });

        leftPanel.add(new JScrollPane(subjectsPanel), BorderLayout.CENTER);
        leftPanel.add(addButton, BorderLayout.NORTH);
        leftPanel.add(deleteButton, BorderLayout.SOUTH);

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
                JComboBox<String> subjectDropdown = new JComboBox<>(teacher.getSubjectsWithRates().keySet().toArray(new String[0]));
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
                    LocalDateTime startTime = (LocalDateTime) startTimeSpinner.getValue();
                    LocalDateTime endTime = (LocalDateTime) endTimeSpinner.getValue();

                    // Convert the Date to LocalDateTime
                    LocalDateTime initial_time = LocalDateTime.ofInstant(day.toInstant(), ZoneId.systemDefault()).withHour(startTime.getHour()).withMinute(startTime.getMinute());
                    LocalDateTime final_time = LocalDateTime.ofInstant(day.toInstant(), ZoneId.systemDefault()).withHour(endTime.getHour()).withMinute(endTime.getMinute());
                    
                    Appointment new_appointment;
					try {
						new_appointment = teacher.createAppointment(initial_time, final_time, subject);
                        appointmentsListModel.addElement(new_appointment);
                        // hasStudent = true;
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
                }
            }
        });
        
        buttonsPanel.add(createAppointmentButton);

        // Delete Appointment Button
        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        deleteAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Appointment selectedAppointment = appointmentsList.getSelectedValue();
                if (selectedAppointment != null) {
                    // VER ESTA PARTE MELHOR
                    // appointments.remove(selectedAppointment);
                    // appointmentsListModel.removeElement(selectedAppointment);
                }
            }
        });
        buttonsPanel.add(deleteAppointmentButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        panel.add(leftPanel);
        panel.add(rightPanel);

        // Set up the JFrame
        setTitle("Combined GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setContentPane(panel);
        setVisible(true);
    }

    // public void buildGui() {
    //     subjects = new ArrayList<>();
    //     rates = new ArrayList<>();
    //     subjectTextFields = new HashMap<>();
    //     appointments = new ArrayList<>();
    //     appointmentSubjects = new String[]{"Math", "Science", "History"};
    //     hasStudent = false;

    //     // Set up the GUI components
    //     JPanel panel = new JPanel();
    //     panel.setLayout(new GridLayout(1, 2));

    //     // Left Panel (MainGui)
    //     JPanel leftPanel = new JPanel();
    //     leftPanel.setLayout(new BorderLayout());

    //     // Subjects Panel
    //     JPanel subjectsPanel = new JPanel();
    //     subjectsPanel.setLayout(new GridLayout(0, 2));

    //     // Add Subject Button
    //     JButton addButton = new JButton("Add Subject");
    //     addButton.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             String subject = JOptionPane.showInputDialog("Enter subject name:");
    //             if (subject != null && !subject.isEmpty()) {
    //                 subjects.add(subject);
    //                 rates.add(0.0);

    //                 // Create a text field for the subject
    //                 JTextField textField = new JTextField();

    //                 textField.addKeyListener(new KeyAdapter() {
    //                     @Override
    //                     public void keyPressed(KeyEvent e) {
    //                         if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    //                             // Update the rate value here

    //                             System.out.println(textField.getText());
    //                             // Perform any necessary operations with the rate value
    //                         }
    //                     }
    //                 });
    //                 subjectTextFields.put(subject, textField);

    //                 // Add a label for the subject
    //                 subjectsPanel.add(new JLabel(subject));
    //                 // Add the text field for the subject
    //                 subjectsPanel.add(textField);

    //                 // Refresh the panel
    //                 subjectsPanel.revalidate();
    //                 subjectsPanel.repaint();
    //             }
    //         }
    //     });

    //     // Delete Subject Button
    //     JButton deleteButton = new JButton("Delete Subject");
    //     deleteButton.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             String selectedSubject = JOptionPane.showInputDialog("Enter subject name to delete:");
    //             if (selectedSubject != null && !selectedSubject.isEmpty()) {
    //                 if (subjects.contains(selectedSubject)) {
    //                     subjects.remove(selectedSubject);
    //                     rates.remove(subjectTextFields.get(selectedSubject));
    //                     subjectTextFields.remove(selectedSubject);

    //                     // Refresh the panel
    //                     subjectsPanel.removeAll();
    //                     for (String subject : subjects) {
    //                         subjectsPanel.add(new JLabel(subject));
    //                         subjectsPanel.add(subjectTextFields.get(subject));
    //                     }
    //                     subjectsPanel.revalidate();
    //                     subjectsPanel.repaint();
    //                 }
    //             }
    //         }
    //     });

    //     leftPanel.add(new JScrollPane(subjectsPanel), BorderLayout.CENTER);
    //     leftPanel.add(addButton, BorderLayout.NORTH);
    //     leftPanel.add(deleteButton, BorderLayout.SOUTH);

    //     // Right Panel (MainFrame)
    //     JPanel rightPanel = new JPanel();
    //     rightPanel.setLayout(new BorderLayout());

    //     // Appointments List
    //     DefaultListModel<Appointment> appointmentsListModel = new DefaultListModel<>();
    //     JList<Appointment> appointmentsList = new JList<>(appointmentsListModel);
    //     appointmentsList.setCellRenderer(new AppointmentListCellRenderer());
    //     JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsList);
    //     rightPanel.add(appointmentsScrollPane, BorderLayout.CENTER);

    //     // Create and Delete Appointment Buttons Panel
    //     JPanel buttonsPanel = new JPanel();
    //     buttonsPanel.setLayout(new FlowLayout());

    //     // Create Appointment Button
    //     JButton createAppointmentButton = new JButton("Create Appointment");
    //     createAppointmentButton.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             JPanel appointmentPanel = new JPanel();
    //             appointmentPanel.setLayout(new GridLayout(5, 2));

    //             // Subject Dropdown
    //             JComboBox<String> subjectDropdown = new JComboBox<>(appointmentSubjects);
    //             appointmentPanel.add(new JLabel("Subject:"));
    //             appointmentPanel.add(subjectDropdown);

    //             // Day Selector
    //             appointmentPanel.add(new JLabel("Day:"));
    //             JSpinner daySpinner = new JSpinner(new SpinnerDateModel());
    //             JSpinner.DateEditor dayEditor = new JSpinner.DateEditor(daySpinner, "dd/MM/yyyy");
    //             daySpinner.setEditor(dayEditor);
    //             appointmentPanel.add(daySpinner);

    //             // Start Time Selector
    //             appointmentPanel.add(new JLabel("Start Time:"));
    //             JSpinner startTimeSpinner = new JSpinner(new SpinnerDateModel());
    //             JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
    //             startTimeSpinner.setEditor(startTimeEditor);
    //             appointmentPanel.add(startTimeSpinner);

    //             // End Time Selector
    //             appointmentPanel.add(new JLabel("End Time:"));
    //             JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
    //             JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
    //             endTimeSpinner.setEditor(endTimeEditor);
    //             appointmentPanel.add(endTimeSpinner);

    //             int result = JOptionPane.showConfirmDialog(null, appointmentPanel, "Create Appointment",
    //                     JOptionPane.OK_CANCEL_OPTION);
    //             if (result == JOptionPane.OK_OPTION) {
    //                 String subject = (String) subjectDropdown.getSelectedItem();
    //                 LocalDate day = LocalDate.parse(dayEditor.getFormat().format(daySpinner.getValue()),
    //                         DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    //                 LocalTime startTime = LocalTime.parse(startTimeEditor.getFormat().format(startTimeSpinner.getValue()));
    //                 LocalTime endTime = LocalTime.parse(endTimeEditor.getFormat().format(endTimeSpinner.getValue()));

    //                 Appointment appointment = new Appointment(subject, day, startTime, endTime, null);
    //                 appointments.add(appointment);
    //                 appointmentsListModel.addElement(appointment);
    //                 hasStudent = true;
    //             }
    //         }
    //     });
    //     buttonsPanel.add(createAppointmentButton);

    //     // Delete Appointment Button
    //     JButton deleteAppointmentButton = new JButton("Delete Appointment");
    //     deleteAppointmentButton.addActionListener(new ActionListener() {
    //         @Override
    //         public void actionPerformed(ActionEvent e) {
    //             Appointment selectedAppointment = appointmentsList.getSelectedValue();
    //             if (selectedAppointment != null) {
    //                 appointments.remove(selectedAppointment);
    //                 appointmentsListModel.removeElement(selectedAppointment);
    //             }
    //         }
    //     });
    //     buttonsPanel.add(deleteAppointmentButton);

    //     rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

    //     panel.add(leftPanel);
    //     panel.add(rightPanel);

    //     // Set up the JFrame
    //     setTitle("Combined GUI");
    //     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     setSize(800, 300);
    //     setLocationRelativeTo(null);
    //     setContentPane(panel);
    //     setVisible(true);
    // }

    // public CombinedGui() {
    //     buildGui();
    // }

    // public static void main(String[] args) {
    //     System.err.println("ENTER THE NAME OF THE TEACHER:");
    //     Scanner scanner = new Scanner(System.in);
    //     String name=scanner.nextLine();
    //     //percorrer lista de professores e ver se existe
    //     SwingUtilities.invokeLater(CombinedGui::new);
    // }

    // static class Appointment {
    //     private String subject;
    //     private LocalDate day;
    //     private LocalTime startTime;
    //     private LocalTime endTime;
    //     private String studentName;

    //     public Appointment(String subject, LocalDate day, LocalTime startTime, LocalTime endTime, String studentName) {
    //         this.subject = subject;
    //         this.day = day;
    //         this.startTime = startTime;
    //         this.endTime = endTime;
    //         this.studentName = studentName;
    //     }

    //     public String getSubject() {
    //         return subject;
    //     }

    //     public LocalDate getDay() {
    //         return day;
    //     }

    //     public LocalTime getStartTime() {
    //         return startTime;
    //     }

    //     public LocalTime getEndTime() {
    //         return endTime;
    //     }

    //     public String getStudentName() {
    //         return studentName;
    //     }

    //     @Override
    //     public String toString() {
    //         return subject + " - " + day + " " + startTime + " to " + endTime + " (Student: " + studentName + ")";
    //     }
    // }

    static class AppointmentListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (renderer instanceof JLabel && value instanceof Appointment) {
                Appointment appointment = (Appointment) value;
                ((JLabel) renderer).setText(appointment.toString());
            }
            return renderer;
        }
    }
}
