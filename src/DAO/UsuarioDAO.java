package DAO;

import DAO.Usuario;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String ARQUIVO = "usuarios.txt";
    private static List<Usuario> usuarios = new ArrayList<>();

    // Carrega usuários do TXT ao iniciar
    static {
        carregarUsuariosDoArquivo();
    }

    public static boolean validarLogin(String nome, String senha) {
        for (Usuario u : usuarios) {
            if (u.getNome().equals(nome) && u.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    public static boolean cadastrarUsuario(String nome, String senha) {
        // Verifica se já existe
        for (Usuario u : usuarios) {
            if (u.getNome().equals(nome)) {
                return false; // já existe
            }
        }

        Usuario novo = new Usuario(nome, senha);
        usuarios.add(novo);
        salvarUsuarioNoArquivo(novo);
        return true;
    }

    // ---- Salvamento no TXT ----
    private static void salvarUsuarioNoArquivo(Usuario u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            writer.write(u.getNome() + ";" + u.getSenha());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    // ---- Carregar do TXT ----
    private static void carregarUsuariosDoArquivo() {
        try {
            if (!Files.exists(Paths.get(ARQUIVO))) {
                return; // arquivo ainda não existe
            }

            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO));

            for (String linha : linhas) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    usuarios.add(new Usuario(partes[0], partes[1]));
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }
}
