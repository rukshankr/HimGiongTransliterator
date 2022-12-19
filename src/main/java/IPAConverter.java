import java.util.HashMap;
import java.util.Map;

public class IPAConverter {
    public IPAConverter () {}

    public String conversion (String sentence) {
        if(sentence.matches(".*\\p{Punct}")) {
            sentence = sentence.substring(0,sentence.length()-1);
        }
        String[] sentArray = sentence.split(" ");
        String output = "/";

        for (String word : sentArray) {
            char[] wordArray = word.toCharArray();
            String transWord = "";
            if(isVowel(wordArray[0])) {
                transWord = beginsWithVowel(wordArray[0]) + word.substring(1);
            }
            else if (word.length() > 3 &&isVowel(wordArray[3])) {
                //chraya
                transWord = beginsWithThreeInitials(word.substring(0,3)) + word.substring(3);
            }
            else if (word.length() > 2 && isVowel(wordArray[2])) {
                //khaya
                transWord = beginsWithTwoInitials(word.substring(0,2)) + word.substring(2);
            }
            else if (word.length() > 1 && isVowel(wordArray[1])) {
                //kan
                transWord = beginsWithOneInitial(wordArray[0]) + word.substring(1);
            }

            //finals
            if (word.length() > 1) {
                transWord = addFinals(transWord);
            }
            //mid vowels
            transWord = processMidVowels(transWord);
            output += transWord + " ";
        }
        return output + "/";
    }

    public static boolean isVowel (char letter) { //TODO :SAME
        char[] vowels = {'a','A','â','e','E','ê','i','y','I','Y','ü','o','O','ô','u','U','û'};

        for(char c: vowels) {
            if(c == letter) {
                return true;
            }
        }
        return false;
    }

    public static String beginsWithVowel (char initial) {
        //Ub -> WB,a -> Wa, i, ah, Uh, y, yng, ya, yang

        String verb = "";

        if (initial == 'i' || initial == 'I') {
            verb = "i";
        } else if (initial == 'U' || initial == 'û') {
            verb = "ɯ";
        } else if (initial == 'a'){
            verb = "a";
        } else if (initial == 'A' || initial == 'â') {
            verb = "æ";
        } else if (initial == 'e') {
            verb = "e";
        } else if (initial == 'E' || initial == 'ê') {
            verb = "ə";
        } else if (initial == 'o') {
            verb = "o";
        } else if (initial == 'O' || initial == 'ô') {
            verb = "ɑː";
        } else if (initial == 'Y' || initial == 'ü') {
            verb = "y";
        } else if (initial == 'y') {
            verb = "ɪː";
        } else if (initial == 'u') {
            verb = "u";
        }

        return verb;
    }

    public static String beginsWithOneInitial(char initial) {
        //k,g,j,t,d,đ,n,p,b,m,r,l,v,w,x,s,z,h,f
        Map<Character, String> initialMap = new HashMap<>();
        initialMap.put('k',"kʰ");  initialMap.put('j',"d͡ʒ");
        initialMap.put('t',"tʰ"); initialMap.put('đ',"d̪");
        initialMap.put('p',"pʰ"); initialMap.put('r',"ɹ");
        initialMap.put('x',"ʃ"); initialMap.put('D',"d̪");

        String init = initialMap.get(initial);
        return (init == null) ? String.valueOf(initial) : init;
    }

    public static String beginsWithTwoInitials(String initials) {
        char first = initials.charAt(0);
        //ch,th,rh,lr,lh,zh, ng
        if (initials.equals("ch")) {
            return "t͡ʃ";
        } else if (initials.equals("th")) {
            return "t̪ʰ";
        } else if (initials.equals("rh")) {
            return "ɾ";
        } else if (initials.equals("lr") || initials.equals("lh")) {
            return "ɭ";
        } else if (initials.equals("zh") || initials.equals("gz")) {
            return "ʒ";
        } else if (initials.equals("ks")) {
            return "(k)ʃ";
        } else if (initials.equals("ng")) {
            return "ŋ";
        } else if (initials.equals("wr")) {
            return "ɹʷ";
        } else if (initials.equals("ss")) {
            return "θ";
        } else if (initials.equals("zz")) {
            return "ð";
        } else if (initials.equals("pv")) {
            return "p͡f";
        }
        //conjunctions
        return beginsWithOneInitial(first) + conjunctInitials(initials.charAt(1));
    }

    public static String beginsWithThreeInitials(String initials) {
        //ch+r, ch+l, kh+l?
        //conjunctions
        if (initials.equals("chr") || initials.equals("chl")) {
            return "ʃ" + conjunctInitials(initials.charAt(2));
        }
        return beginsWithTwoInitials(initials.substring(0,2)) + conjunctInitials(initials.charAt(2));
    }

    public static String conjunctInitials (char conj) {
        //_r
        if (conj == 'r') {
            return "ɹ";
        }
        //_h
        else if (conj == 'h') {
            return "ʁ";
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
                return rest + "ʃ";
            } else if (fin.equals("ngk")) {
                return rest + "ŋk";
            } else if (fin.equals("ngs")) {
                return rest + "ŋs";
            } else if (fin.equals("ngd")) {
                return rest + "ŋd";
            } else if (fin.equals("ngl")) {
                return rest + "ŋl";
            } else if (fin.equals("ths")) {
                return rest + "t̪s";
            } else if (fin.equals("sth")) {
                return rest + "st̪";
            }
        }
        //Wsk | Wng | Wua | Wll | Mue_sk | W_aya
        if (word.length() > 2) {
            fin = word.substring(word.length() - 2);
            rest = word.substring(0,word.length() - 2);
            if (fin.equals("ng")) {
                return rest + "ŋ";
            }
            else if (fin.equals("th")) {
                return rest + "t̪";
            }
            else {
                char[] finals = fin.toCharArray();
                if (finals[0] == 'w') {
                    return rest + 'w' + oneFinal(finals[1]);
                }
                if (!isVowel(finals[1])) {
                    if (finals[0] == finals[1]) {
                        return rest + oneFinal(finals[0]);
                    }
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
            if (fin == 'đ' || fin == 'D') {
                return "t̪";
            } else if (fin == 'd') {
                return "ɻ";
            } else if (fin == 'j') {
                return "t͡ʃ";
            } else if (fin == 'b') {
                return "p";
            }
        }
        return String.valueOf(fin);
    }

    public static String processMidVowels (String word) {
        char[] wordArr = word.toCharArray();
        String newWord = "";

        for (char c: wordArr) {
            if(isVowel(c)) {
                if (c == 'y') {
                    newWord += 'j';
                    continue;
                } else {
                    newWord += beginsWithVowel(c);
                    continue;
                }
            }
            newWord += String.valueOf(c);
        }
        return newWord;
    }
}

