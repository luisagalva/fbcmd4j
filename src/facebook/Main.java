package facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook.Utils;

import facebook4j.Facebook;
import facebook4j.FacebookException;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";
	private static final String APP_VERSION = "v1.0";

	public static void main(String[] args) {
		logger.info("Iniciando app");
		Facebook facebook = null;
		
		int seleccion;
		try (Scanner scanner = new Scanner(System.in)){
			while(true){
				
				// Inicio Menu
				System.out.format("Simple Facebook client %s\n\n", APP_VERSION);
				System.out.println("Opciones: ");
				System.out.println("(0) Iniciar Sesión");
				System.out.println("(1) Obtener Newsfeed");
				System.out.println("(2) Wall");
				System.out.println("(3) Publicar estado");
				System.out.println("(4) Publicar link");
				System.out.println("(5) Salir");
				System.out.println("\nPor favor ingrese una opción: ");
				// Fin de Menu
				try {
					seleccion = scanner.nextInt();
			

					switch(seleccion){
						case 0:
							facebook = Utils.generate();
							break;
						case 1: 
							 Utils.postFeed(facebook.getFeed(), scanner);
							break;
						
						case 2:
							Utils.postFeed(facebook.getPosts(), scanner);
							break;
						case 3:
							System.out.println("Escribe tu estado: ");
							String estado = scanner.nextLine();
							facebook.postStatusMessage(estado);
							
							break;
						case 4:
							System.out.println("Escribe tu link: ");
							String link = scanner.nextLine();
							System.out.println("y el mensaje que lo acompaña: ");
							String message= scanner.nextLine();
							facebook.postLink(new URL(link),message);

							// 2. Por medio del usuario ingresado en userStr invocar el método
							// Utils.infoUsuario para imprimir la información
							// Escribe tu codigo aqui {
							
							//}
							break;
						case 5:
							System.exit(0);
						default:
							logger.error("Opción inválida");
							break;
					}
				} catch (InputMismatchException ex){
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error("Opción inválida. %s. \n", ex.getClass());
					scanner.next();} catch (FacebookException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						logger.error(e);
					} 

			}
			}
	
		}
}
