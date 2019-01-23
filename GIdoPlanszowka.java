// Gra parcelacja
// Autor: Marek Skrzypkowski
//                         Zasady:
//gra polega na wypełnieniu całej planszy np pięcioma kwadratami
//z "piątką" w tym kwadracie i owe kwadraty muszę się wszystkie stykać

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.lang.*;


class ZeroException extends Exception
{
    double e;
}

//mechanika gry
class Model implements Serializable
{
    Integer  tab[][];// = new Integer[8][7];
    int liczby[] = {5, 6, 7, 8, 9, 10, 11};
    Color tab_kolory[][];//=new Color[8][7];
    Color kolory[] = {new Color(15,161,198),new Color(24,158,73),new Color(114,61,151),
            new Color(218,117,175),new Color(243,118,86), new Color(246,239,91),
            new Color(238,47,47)};
    ArrayList<GIdoPlanszowka.Kwadrat> poprzednie_stany = new ArrayList<>();
    ArrayList<GIdoPlanszowka.Kwadrat> nastepne_stany = new ArrayList<>();
    Integer odpowiedz[][]={
            {6,6,8,8,10,10,11},
            {6,6,8,8,10,10,11},
            {6,6,8,9,10,10,11},
            {5,7,8,9,10,10,11},
            {5,7,8,9,10,10,11},
            {5,7,8,9,9,11,11},
            {5,7,7,9,9,11,11},
            {5,7,7,9,9,11,11}};



    void setPoprzednie_stany(GIdoPlanszowka.Kwadrat a) {poprzednie_stany.add(a);}
    Model(int i, int j)
    {
        tab = new Integer[i][j];
        tab_kolory = new Color[i][j];

        //ustawianie na sztywno wartosci początkowych
        tab[1][1]=6;
        tab_kolory[1][1]=kolory[1];

        tab[3][0]=5;
        tab_kolory[3][0]=kolory[0];

        tab[1][3]=8;
        tab_kolory[1][3]=kolory[3];

        tab[7][2]=7;
        tab_kolory[7][2]=kolory[2];

        tab[5][3]=9;
        tab_kolory[5][3]=kolory[4];

        tab[2][4]=10;
        tab_kolory[2][4]=kolory[5];

        tab[7][6]=11;
        tab_kolory[7][6]=kolory[6];

    }


    }

//graficzny interfejs gry
public class GIdoPlanszowka extends JFrame implements Serializable
{
    Model model;
    Integer obecna_liczba; //służy do przechowania jaka liczba będzie wstawiana
    Color obecny_kolor;//przechowuje jaki kolor będzie wstawiany

    JButton tab[][] = new JButton[8][7];
    JButton liczby[]= new JButton[7];
    JButton
            help = new JButton("Help"),
            zapisz = new JButton("Zapisz"),
            dotylu = new JButton("<--"),
            doprzodu = new JButton("-->"),
            sprawdz = new JButton("Sprawdź");

    JPanel plansza = new JPanel();
    JPanel sterowanie = new JPanel();
    JTextField t = new JTextField(10);
    Font font1 = new Font("SansSerif", Font.BOLD, 25);
    Font font2 = new Font("Arial", Font.CENTER_BASELINE, 25);

    public GIdoPlanszowka(Model model) {
        this.model=model;
        setTitle("GRA");
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(2,0));
        cp.add(plansza); cp.add(sterowanie);
        sterowanie.setLayout(null);
        //t.setFont(t.getFont().deriveFont(30.0f));

        plansza.setLayout(new GridLayout(8,8));
        for (int i=0;i<8;i++)
            for (int j=0;j<7;j++){
                tab[i][j]=new JButton("");
                plansza.add(tab[i][j]);
                if(model.tab[i][j]!=null)
                {
                    (tab[i][j]).setText(String.valueOf(model.tab[i][j]));
                    (tab[i][j]).setBackground(model.tab_kolory[i][j]);

                    //początkowe kafelki
                    if(i==1&&j==1) {
                        tab[i][j].setEnabled(false);
                    }
                    else if(i==3&&j==0)
                    {
                        (tab[i][j]).setText("5");
                        (model.tab[i][j])=5;
                        tab[i][j].setEnabled(false);

                    }
                    else if(i==1&&j==3)
                    {
                        (tab[i][j]).setText("8");
                        (model.tab[i][j])=8;
                        tab[i][j].setEnabled(false);
                    }
                    else if(i==7&&j==2)
                    {
                        (tab[i][j]).setText("7");
                        (model.tab[i][j])=7;
                        tab[i][j].setEnabled(false);
                    }
                    else if(i==5&&j==3)
                    {
                        (tab[i][j]).setText("9");
                        (model.tab[i][j])=9;
                        tab[i][j].setEnabled(false);
                    }
                    else if(i==2&&j==4)
                    {
                        (tab[i][j]).setText("10");
                        (model.tab[i][j])=10;
                        tab[i][j].setEnabled(false);
                    }
                    else if(i==7&&j==6)
                    {
                        (tab[i][j]).setText("11");
                        (model.tab[i][j])=11;
                        tab[i][j].setEnabled(false);
                    }

                }
                (tab[i][j]).addActionListener(new B(i,j));
                (tab[i][j]).setBorder(new LineBorder(Color.black));
            }

        Insets insets = sterowanie.getInsets();

         //przyciski z liczbami do wyboru
        for(int i=0;i<7;i++)
        {

            liczby[i]= new JButton(String.valueOf(model.liczby[i]));
            liczby[i].setBounds(60+10*i, 400+10*i, 20, 30);
            Dimension size = new Dimension(70,60);
            liczby[i].setBounds(15 +i*82+ insets.left, 40 + insets.top, size.width, size.height);
            liczby[i].setFont(font1);
            sterowanie.add(liczby[i]);
            liczby[i].setBackground(model.kolory[i]);
            liczby[i].addActionListener(new A(i));
        }
        Dimension size = new Dimension(520,70);
        t.setBounds(40 + insets.left, 400 + insets.top, size.width, size.height);
        t.setFont(font2);
        sterowanie.add(t);
        t.setHorizontalAlignment(JTextField.CENTER);

        size = new Dimension(150,100);
        help.setBounds(30 + insets.left, 200 + insets.top, size.width, size.height);
        sterowanie.add(help);
        help.setFont(font2);
        help.addActionListener(new Help());

        zapisz.setBounds(190 + insets.left, 200 + insets.top, size.width, size.height);
        sterowanie.add(zapisz);
        zapisz.setFont(font2);
        zapisz.addActionListener(new Zapisz());

        size = new Dimension(100,100);
        dotylu.setBounds(350 + insets.left, 200 + insets.top, size.width, size.height);
        sterowanie.add(dotylu);
        dotylu.setFont(font2);
        dotylu.addActionListener(new Cofnij());

        doprzodu.setBounds(460 + insets.left, 200 + insets.top, size.width, size.height);
        sterowanie.add(doprzodu);
        doprzodu.setFont(font2);
        doprzodu.addActionListener(new DoPrzodu());

        size = new Dimension(250, 50);
        sprawdz.setBounds(160 + insets.left, 325 + insets.top, size.width, size.height);
        sterowanie.add(sprawdz);
        sprawdz.setFont(font2);
        sprawdz.addActionListener(new Sprawdz());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //klasa kwadrat przechowuje stare kwadraty (ruchy)
    class Kwadrat implements Serializable
    {
        Integer liczba;
        Color kolor;
        int i;
        int j;
        Kwadrat(Integer liczba, Color kolor, int i, int j)
        {
            this.liczba = liczba;
            this.kolor = kolor;
            this.i = i;
            this.j = j;
        }
    }
    class B implements ActionListener {
        int i,j;
        B(int i,int j){this.i=i;this.j=j;}

        //naciśnięcie klawisza z planszy ustawia napis oraz kolor przycisku
        //oraz zmienia dane programu z klasy model
        public void actionPerformed(ActionEvent e)
        {
            try {
                model.nastepne_stany.clear();   //czyści poprzednie stany, bo jak wstawimy nowa liczbe
                if(obecna_liczba==null)         //to juz nie bd chceili isc do przodu
                    throw new ZeroException();
                Kwadrat k = new Kwadrat(obecna_liczba, obecny_kolor, i, j);
                model.setPoprzednie_stany(k);
                (tab[i][j]).setBackground(obecny_kolor);
                (tab[i][j]).setText(String.valueOf(obecna_liczba));
                (model.tab[i][j]) = obecna_liczba;
                (model.tab_kolory[i][j]) = obecny_kolor;
            }catch(ZeroException q) {}
        }
    }
    class A implements ActionListener
    {
        int i;
        A(int i){this.i=i;}
        //metoda która ustawia na obecny kolor kolorowania i liczbę do wstawiania
        //na liczbę z klikniętego kwadratu
        public void actionPerformed(ActionEvent e)
        {
            obecna_liczba = model.liczby[i];
            obecny_kolor = model.kolory[i];
        }
    }

    //pobiera obiekt z listy obiektów poprzednich i z jego danymi ustawia to pole na puste
    //dodaje obiekt do następnych stanów
    class Cofnij implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(!model.poprzednie_stany.isEmpty())
            {
                Kwadrat k = model.poprzednie_stany.get(model.poprzednie_stany.size() - 1);
                model.tab[k.i][k.j] = null;
                tab[k.i][k.j].setText("");
                tab[k.i][k.j].setBackground(null);
                model.poprzednie_stany.remove(model.poprzednie_stany.size() - 1);
                model.nastepne_stany.add(k);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nie można cofnąć");
            }
        }
    }

    class DoPrzodu implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(!(model.nastepne_stany.isEmpty()))
            {
                Kwadrat k = model.nastepne_stany.get(model.nastepne_stany.size() - 1);
                model.tab[k.i][k.j] = k.liczba;
                tab[k.i][k.j].setText(String.valueOf(model.tab[k.i][k.j]));
                tab[k.i][k.j].setBackground(k.kolor);
                model.nastepne_stany.remove(model.nastepne_stany.size() - 1);
                model.poprzednie_stany.add(k);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Nie można iść do przodu.");
            }
        }
    }

    //Zapisuje do pliku podanego przez użytkownika
    class Zapisz implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                String plik;
                plik = JOptionPane.showInputDialog("Do jakiego pliku zapisać?");
                FileOutputStream f = new FileOutputStream(plik);
                ObjectOutputStream os = new ObjectOutputStream(f);
                os.writeObject(model);
                f.close();
            } catch (IOException e1) { }

        }
    }

    //jeżeli plansza nie uzupełniona do końca to ujawnia po jednym kafelku z dobrego rozwiązania
    //jeżeli cała plansza uzupełniona to pisze których licz za dużo
    class Help implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            for (int i=0;i<8;i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if(model.tab[i][j]==null)
                    {
                        model.tab[i][j]=model.odpowiedz[i][j];
                        tab[i][j].setText(String.valueOf(model.odpowiedz[i][j]));
                        Color a;
                        a = model.kolory[model.odpowiedz[i][j]-5];
                        tab[i][j].setBackground(a);
                        return;
                    }
                }
            }

            Integer[] suma = new Integer[7];
            Arrays.fill(suma, 0);
            for(int z=0; z<7;z++)// !!!!!!z to liczba liczb do kombinacji
            {
                for (int i=0;i<8;i++)
                {
                    for (int j = 0; j < 7; j++)
                    {
                        if(model.tab[i][j] == model.liczby[z])
                        {
                            suma[z]++;
                        }
                    }
                }
            }

            for(int z=0; z<7;z++)
            {
                if(!suma[z].equals(model.liczby[z]))
                {
                    Integer sub = model.liczby[z]-suma[z];
                    t.setText("O "+sub+" za malo "+"'"+model.liczby[z]+"'");
                    return;
                }
            }


        }

    }

    //Liczy czy jest odpowiednia ilosc danej liczby
    //później sprawdza czy znajduje się ten kafelek koło kafelka o tym samym kolorze
    class Sprawdz implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {

            for (int i=0;i<8;i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if(model.tab[i][j] == null)
                    {
                        JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                        return;
                    }
                }
            }
            //dobry sposób sprawdzania wyspy
            class Islands {

                //No of rows and columns
                static final int ROW = 8, COL = 7;

                // A function to check if a given cell (row, col) can
                // be included in DFS
                boolean isSafe(Integer M[][], int row, int col,
                               boolean visited[][], int liczba) {
                    // row number is in range, column number is in range
                    // and value is 1 and not yet visited
                    return (row >= 0) && (row < ROW) &&
                            (col >= 0) && (col < COL) &&
                            (M[row][col] == liczba && !visited[row][col]);
                }

                // A utility function to do DFS for a 2D boolean matrix.
                // It only considers the 8 neighbors as adjacent vertices
                void DFS(Integer M[][], int row, int col, boolean visited[][], int liczba) {
                    // These arrays are used to get row and column numbers
                    // of 8 neighbors of a given cell
                    int rowNbr[] = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
                    int colNbr[] = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};

                    // Mark this cell as visited
                    visited[row][col] = true;

                    // Recur for all connected neighbours
                    for (int k = 0; k < 8; ++k)
                        if (isSafe(M, row + rowNbr[k], col + colNbr[k], visited, liczba))
                            DFS(M, row + rowNbr[k], col + colNbr[k], visited, liczba);
                }

                // The main function that returns count of islands in a given
                //  boolean 2D matrix
                int countIslands(Integer M[][], int liczba) {
                    // Make a bool array to mark visited cells.
                    // Initially all cells are unvisited
                    boolean visited[][] = new boolean[ROW][COL];


                    // Initialize count as 0 and travese through the all cells
                    // of given matrix
                    int count = 0;
                    for (int i = 0; i < ROW; ++i)
                        for (int j = 0; j < COL; ++j)
                            if (M[i][j] == liczba && !visited[i][j]) // If a cell with
                            {                                 // value 1 is not
                                // visited yet, then new island found, Visit all
                                // cells in this island and increment island count
                                DFS(M, i, j, visited, liczba);
                                ++count;
                            }

                    return count;
                }
            }
            Islands I = new Islands();
            for(int i=5; i<12; i++)
            {
                if (I.countIslands(model.tab, i) > 1)
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
            }


            /*
            //własny sposob sprawdzania nie pełny
            Integer[] suma = new Integer[7];
            Arrays.fill(suma, 0);
            for(int z=0; z<7;z++)// !!!!!!z to liczba liczb do kombinacji
            {
                for (int i=0;i<8;i++)
                {
                    for (int j = 0; j < 7; j++)
                    {
                        if(model.tab[i][j] == model.liczby[z])
                        {
                            suma[z]++;


                        }
                    }
                }
            }

            for(int z=0; z<7; z++)
            {
                if(suma[z] != model.liczby[z])
                {
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                    return;
                }
            }

            int i=0;
            int j=0;
            if(!model.tab[i][j].equals(model.tab[i][j + 1]) && !model.tab[i][j].equals(model.tab[i + 1][j]))
            {
                JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                return;
            }

            i=7;
            j=0;
            if(!model.tab[i][j].equals(model.tab[i][j + 1]) && !model.tab[i][j].equals(model.tab[i - 1][j]))
            {
                JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                return;
            }

            i=0;
            j=6;
            if(!model.tab[i][j].equals(model.tab[i][j - 1]) && !model.tab[i][j].equals(model.tab[i + 1][j]))
            {
                JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                return;
            }

            i=7;
            j=6;
            if(!model.tab[i][j].equals(model.tab[i][j - 1]) && !model.tab[i][j].equals(model.tab[i - 1][j]))
            {
                JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                return;
            }

            for(i=1; i<=6; i++)
            {
                j=0;
                if(!model.tab[i][j].equals(model.tab[i - 1][j]) && !model.tab[i][j].equals(model.tab[i + 1][j]) && !model.tab[i][j].equals(model.tab[i][j + 1]))
                {
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                    return;
                }
            }

            for(i=1; i<=6; i++)
            {
                j=6;
                if(!model.tab[i][j].equals(model.tab[i - 1][j]) && !model.tab[i][j].equals(model.tab[i + 1][j]) && !model.tab[i][j].equals(model.tab[i][j - 1]))
                {
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                    return;
                }
            }

            for(j=1; j<=5; j++)
            {
                i=0;
                if(!model.tab[i][j].equals(model.tab[i][j - 1]) && !model.tab[i][j].equals(model.tab[i][j + 1]) && !model.tab[i][j].equals(model.tab[i + 1][j]))
                {
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                    return;
                }
            }

            for(j=1; j<=5; j++)
            {
                i=7;
                if(!model.tab[i][j].equals(model.tab[i][j - 1]) && !model.tab[i][j].equals(model.tab[i][j + 1]) && !model.tab[i][j].equals(model.tab[i - 1][j]))
                {
                    JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                    return;
                }
            }

            for(i=1; i<=6; i++)
            {
                for(j=1; j<=5;j++)
                {
                    if(!model.tab[i][j].equals(model.tab[i][j - 1]) && !model.tab[i][j].equals(model.tab[i][j + 1]) && !model.tab[i][j].equals(model.tab[i - 1][j]) && !model.tab[i][j].equals(model.tab[i + 1][j]))
                    {
                        JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                        return;
                    }
                }
            }*/

            t.setText("Dobra odpowiedź");

            //sztywny sposob sprawdzania
            /*
            Integer[][] odpowiedz = {
                    {6, 6, 8, 8, 10, 10, 11},
                    {6, 6, 8, 8, 10, 10, 11},
                    {6, 6, 8, 9, 10, 10, 11},
                    {5, 7, 8, 9, 10, 10, 11},
                    {5, 7, 8, 9, 10, 10, 11},
                    {5, 7, 8, 9, 9, 11, 11},
                    {5, 7, 7, 9, 9, 11, 11},
                    {5, 7, 7, 9, 9, 11, 11}};
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if (model.tab[i][j] == null) {
                        JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                        return;
                    }
                    if(!model.tab[i][j].equals(odpowiedz[i][j]))
                    {
                        JOptionPane.showMessageDialog(null, "Zła odpowiedź");
                        return;
                    }

                }
            }
            t.setText("Dobra odpowiedz");*/

        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Model m;// = new Model();
        Scanner odczyt = new Scanner(new File("generacja.txt"));
        int i = Integer.valueOf(odczyt.nextLine());
        int j = Integer.valueOf(odczyt.nextLine());


        try {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Czy wczytać grę z pliku",
                                                                                "Title on Box", dialogButton);
            if(dialogResult == 0)
            {
                String plik1;
                plik1 = JOptionPane.showInputDialog("Z jakiego pliku?");

                ObjectInputStream is = new ObjectInputStream(
                        new FileInputStream(plik1));
                m = (Model) is.readObject();
                is.close();
            } else {
                m = new Model(i, j);
            }


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "nie ma takiego pliku.");
            m = new Model(i, j);
        } catch (ClassNotFoundException e) { m = new Model(i, j);}

        JFrame f = new GIdoPlanszowka(m);

        Insets insets = f.getInsets();
        f.setSize(600 + insets.left + insets.right,
                1000 + insets.top + insets.bottom);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        //lokalizacja na środku ekranu
        f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2 -20);

        f.setResizable(false);

        f.setVisible(true);
    }
}