
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextToFont {
    private JTextField InputText;
    private JButton ConvertButton;
    private JTextArea fontArea;
    private JPanel rootPanel;
    private JLabel title;
    private JButton âButton;
    private JButton đButton;
    private JButton êButton;
    private JButton üButton;
    private JButton ûButton;
    private JButton ôButton;
    private JTextField ipaOutput;
    private JButton ipaButton;

    public TextToFont() {
        ConvertButton.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conversion();
            }
        });
        InputText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                promptTextFocus();
            }
            @Override public void focusLost(FocusEvent e) {
                if (InputText.getText().isEmpty()) {
                    InputText.setText("enter your text here...");
                }
            }
        });
        InputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    conversion();
                }
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptTextFocus();
                if (âButton.isFocusOwner()){
                    InputText.setText(InputText.getText()+ "â");
                } else if (đButton.isFocusOwner()) {
                    InputText.setText(InputText.getText()+ "đ");
                } else if (êButton.isFocusOwner()) {
                    InputText.setText(InputText.getText()+ "ê");
                } else if (üButton.isFocusOwner()) {
                    InputText.setText(InputText.getText()+ "ü");
                } else if (ôButton.isFocusOwner()) {
                    InputText.setText(InputText.getText()+ "ô");
                } else if (ûButton.isFocusOwner()) {
                    InputText.setText(InputText.getText()+ "û");
                }
                InputText.requestFocusInWindow();
            }
        };
        âButton.addActionListener(listener);
        đButton.addActionListener(listener);
        êButton.addActionListener(listener);
        üButton.addActionListener(listener);
        ûButton.addActionListener(listener);
        ôButton.addActionListener(listener);

        ipaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IPAConverter ipaConverter = new IPAConverter();
                String output = ipaConverter.conversion(InputText.getText());
                ipaOutput.setText(output);
            }
        });
    }

    public void promptTextFocus () {
        if (InputText.getText().equals("enter your text here...")) {
            InputText.setText("");
        }
    }

    public void conversion() {
        String input = InputText.getText();

        InputStream is = TextToFont.class.getResourceAsStream("Fuot.otf");
        //File is = new File("F:\\Fonts\\Fuot.otf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            //GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            //ligatures
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
            font = font.deriveFont(((Map<TextAttribute, ?>) attributes));

            Font sizedFuot = font.deriveFont(48f);
            fontArea.setFont(sizedFuot);
            fontArea.setText(convert(input));
        } catch (FontFormatException | IOException ex) {
            fontArea.setText("Font Not Created :( \n"+ ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Him Giông Text To Font");
        frame.setContentPane(new TextToFont().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        //frame.pack();
        frame.setVisible(true);
    }

    public static String convert (String sentence) {
        String endPunct = "";
        if(sentence.matches(".*\\p{Punct}")) {
            endPunct = sentence.substring(sentence.length()-1);
            sentence = sentence.substring(0,sentence.length()-1);
        }
        String[] sentArray = sentence.split(" ");
        String output = "";

        for (String word : sentArray) {
            char[] wordArray = word.toCharArray();
            String transWord = "";
            if(isVowel(wordArray[0])) {
                transWord = beginsWithVowel(word, wordArray[0]);
            }
            else if (word.length() > 1 && isVowel(wordArray[1])) {
                //kan
                transWord = beginsWithOneInitial(wordArray[0]) + word.substring(1);
            }
            else if (word.length() > 2 && isVowel(wordArray[2])) {
                //khaya
                transWord = beginsWithTwoInitials(word.substring(0,2)) + word.substring(2);
            }
            else if (word.length() > 3 &&isVowel(wordArray[3])) {
                //chraya
                transWord = beginsWithThreeInitials(word.substring(0,3)) + word.substring(3);
            }
            //finals
            if (word.length() > 1) {
                transWord = addFinals(transWord);
            }
            //mid vowels
            transWord = processMidVowels(transWord);
            output += transWord + " ";
        }
        return output + endPunct;
    }

    public static boolean isVowel (char letter) {
        char[] vowels = {'a','A','â','e','E','ê','i','y','I','Y','ü','o','O','ô','u','U','û'};

        for(char c: vowels) {
            if(c == letter) {
                return true;
            }
        }
        return false;
    }

    public static String beginsWithVowel (String word, char initial) {
        //Ub -> WB,a -> Wa, i, ah, Uh, y, yng, ya, yang

        StringBuilder verb = new StringBuilder();

        if (initial == 'i' || initial == 'I') {
            verb.append("I");
        } else if (initial == 'U' || initial == 'û') {
            verb.append("W");
        } else if (initial == 'a'){
            verb.append("Wa");
        } else if (initial == 'A' || initial == 'â') {
            verb.append("WA");
        } else if (initial == 'e') {
            verb.append("We");
        } else if (initial == 'E' || initial == 'ê') {
            verb.append("WE");
        } else if (initial == 'o') {
            verb.append("Wo");
        } else if (initial == 'O' || initial == 'ô') {
            verb.append("WO");
        } else if (initial == 'Y' || initial == 'ü') {
            verb.append("IY");
        } else if (initial == 'y') {
            verb.append("cw");
        } else if (initial == 'u') {
            verb.append("Wu");
        }

        return verb.append(word.substring(1)).toString();
    }

    public static String beginsWithOneInitial(char initial) {
        //k,g,j,t,d,đ,n,p,b,m,r,l,v,w,x,s,z,h,f
        Map<Character, String> initialMap = new HashMap<>();
        initialMap.put('k',"kg"); initialMap.put('g',"kb"); initialMap.put('j',"cb");
        initialMap.put('t',"tg"); initialMap.put('d',"tb"); initialMap.put('đ',"db");
        initialMap.put('n',"sn"); initialMap.put('p',"mg"); initialMap.put('b',"mb");
        initialMap.put('m',"mn"); initialMap.put('r',"sr"); initialMap.put('l',"sl");
        initialMap.put('v',"mv"); initialMap.put('f',"mf"); initialMap.put('w',"mw");
        initialMap.put('x',"cf"); initialMap.put('s',"sf"); initialMap.put('z',"sv");
        initialMap.put('h',"kf"); initialMap.put('D',"db");

        String init = initialMap.get(initial);
        return (init == null) ? String.valueOf(initial) : init;
    }

    public static String beginsWithTwoInitials(String initials) {
        char first = initials.charAt(0);
        //ch,th,rh,lr,lh,zh, ng
        if (initials.equals("ch")) {
            return "cg";
        }
        else if (initials.equals("th")) {
            return "dg";
        }
        else if (initials.equals("rh")) {
            return "tr";
        }
        else if (initials.equals("lr") || initials.equals("lh")) {
            return "tl";
        }
        else if (initials.equals("zh")) {
            return "cv";
        }
        else if (initials.equals("ng")) {
            return "kn";
        }
        //conjunctions
        return beginsWithOneInitial(first) + conjunctInitials(initials.charAt(1));

        //return  initials;
    }

    public static String beginsWithThreeInitials(String initials) {
        //ch+r, ch+l, kh+l?
        //conjunctions
        return beginsWithTwoInitials(initials.substring(0,2)) + conjunctInitials(initials.charAt(2));
    }

    public static String conjunctInitials (char conj) {
        //_r
        if (conj == 'r') {
            return "R";
        }
        //_l
        else if (conj == 'l') {
            return "l";
        }
        //_h
        else if (conj == 'h') {
            return "h";
        }
        //_s
        else if (conj == 's' || conj == 'z') {
            return "z";
        }
        //_v
        else if (conj == 'v' || conj == 'f') {
            return "V";
        }
        return String.valueOf(conj);
    }

    public static String addFinals (String word) {
        String fin = "";
        String rest = "";
        //W_jch | bruey_ong | W_aya | Mu_esk | la_ngk | ra_ngs
        if (word.length() > 3) {
            fin = word.substring(word.length() - 3);
            rest = word.substring(0,word.length() - 3);
            if (fin.equals("jch")) {
                return rest + "C";
            } else if (fin.equals("ngk")) {
                return rest + "XK";
            } else if (fin.equals("ngs")) {
                return rest + "XS";
            } else if (fin.equals("ngd")) {
                return rest + "XD";
            } else if (fin.equals("ngl")) {
                return rest + "XL";
            } else if (fin.equals("ths")) {
                return rest + "QS";
            } else if (fin.equals("sth")) {
                return rest + "SQ";
            }
        }
        //Wsk | Wng | Wua | Wll | Mue_sk | W_aya
        if (word.length() > 2) {
            fin = word.substring(word.length() - 2);
            rest = word.substring(0,word.length() - 2);
            if (fin.equals("ng")) {
                return rest + "X";
            }
            else if (fin.equals("th")) {
                return rest + "Q";
            }
            else {
                char[] finals = fin.toCharArray();
                if (finals[0] == 'w') {
                    return rest + 'w' + oneFinal(finals[1]);
                }
                if (!isVowel(finals[1])) {
                    return rest + oneFinal(finals[0]) + oneFinal(finals[1]);
                }
            }
        }
        //Wk | Wl | Wa | svzO
        if (word.length() > 1) {
            fin = word.substring(1);

            if (fin.length() == 1) {
                return word.substring(0,1) + oneFinal(fin.charAt(0));
            }

        }

        //no finals: Waya
        return word;
    }

    public static String oneFinal (char fin) {
        if (!isVowel(fin)) {
            return (fin == 'đ' || fin == 'D') ? "Q" : String.valueOf(Character.toUpperCase(fin));
        }
        else return String.valueOf(fin);
    }

    public static String processMidVowels (String word) {
        char[] wordArr = word.toCharArray();
        String newWord = "";
        Map<Character, String> vowelMap = new HashMap<>();

        vowelMap.put('â', "A"); vowelMap.put('ê', "E"); vowelMap.put('ü', "Y");
        vowelMap.put('ô', "O"); vowelMap.put('û', ""); vowelMap.put('U', "");

        for (char c: wordArr) {
            if(isVowel(c)) {
                String v = vowelMap.get(c);
                if (v != null) {
                    newWord += v;
                    continue;
                }
            }
            newWord += String.valueOf(c);
        }
        return newWord;
    }
}
