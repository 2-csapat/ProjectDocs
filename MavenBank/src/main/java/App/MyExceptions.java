
package App;

import javax.swing.JOptionPane;

public class MyExceptions {

    public static class UsedUserName extends Exception {
        
        public UsedUserName() {
            JOptionPane.showMessageDialog(null, "Használt felhasználónév.");
        }
    }
    
    public static class BadUserName extends Exception {
        
        public BadUserName() {
            JOptionPane.showMessageDialog(null, "Nem megfelelő felhasználónév.");
        }
    }
    
    public static class UsedEmail extends Exception {
        
        public UsedEmail() {
            JOptionPane.showMessageDialog(null, "Használt email cím.");
        }
    }
    
    public static class Short extends Exception {

        public Short() {
            JOptionPane.showMessageDialog(null, "Jelszónak minimum 12 karakternek kell lennie.");
        }
    }

    public static class NoUpperc extends Exception {

        public NoUpperc() {
            JOptionPane.showMessageDialog(null, "Jelszónak tartalmaznia kell nagy betűt.");
        }
    }

    public static class NoLowerC extends Exception {

        public NoLowerC() {
            JOptionPane.showMessageDialog(null, "Jelszónak tartalmaznia kell kis betűt.");
        }
    }

    public static class NoNumber extends Exception {

        public NoNumber() {
            JOptionPane.showMessageDialog(null, "Jelszónak tartalmaznia kell számot.");
        }
    }

    public static class Empty extends Exception {

        public Empty() {
            JOptionPane.showMessageDialog(null, "Jelszó nem lehet üres.");
        }
    }

    public static class NoSpecial extends Exception {

        public NoSpecial() {
            JOptionPane.showMessageDialog(null, "Jelszónak tartalmazniak kell speciális karaktert.");
        }
    }
    
    public static class Space extends Exception {
        
        public Space() {
            JOptionPane. showMessageDialog(null, "Jelszó nem tartalmazhat szóközt.");
        }
    }
    
    public static class DiffPw extends Exception {
        
        public DiffPw() {
            JOptionPane.showMessageDialog(null, "A két jelszó eltérő.");
        }
    }
    
    public static class InvalidPhoneNum extends Exception {
        
        public InvalidPhoneNum() {
            JOptionPane.showMessageDialog(null, "Nem megfelelő telefonszám, írja a következő minta szerint: +36/xxxxxxxxx");
        }
    }
    
    public static class InvalidName extends Exception {
        public InvalidName() {
            JOptionPane.showMessageDialog(null, "Nem megfelelő név.");
        }
    }
    
    public static class InsufficientFunds extends Exception {
        public InsufficientFunds() {
            JOptionPane.showMessageDialog(null, "Nem megfelelő érték");
        }
    }
}
