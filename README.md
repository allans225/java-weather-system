# 🌩️ JWeather System (Versão 1.0 - Terminal)

Este projeto é um sistema de consulta meteorológica desenvolvido em **Java Nativo**. Ele consome dados em tempo real da 
[WeatherAPI](https://www.weatherapi.com/) e processa arquivos JSON para exibir informações climáticas organizadas no terminal.

## 🚀 Evolução do Projeto
* **v1.0 (Atual):** Aplicação Console com gerenciamento manual de dependências e variáveis de ambiente.
* **v2.0 (Planejado):** Migração para **Spring Boot** com automação via Maven/Gradle e integração com a biblioteca de 
mapas **Leaflet**.

## 🛠️ Configuração do Ambiente no IntelliJ

Como esta versão utiliza Java puro sem gerenciadores de pacotes (como Maven), siga estes passos para rodar o projeto localmente:

### 1. Variável de Ambiente (API Key)
O sistema busca a chave da API através de uma variável de ambiente por segurança.
> a) Vá em **Run -> Edit Configurations**.

> b) No campo **Environment Variables**, adicione: `JWEATHER_KEY=sua_chave_aqui`.
*Dica: Você pode encontrar um modelo no arquivo `.env.example` na raiz do projeto.*

### 2. Dependência JSON
O projeto utiliza a biblioteca `org.json` para manipular os dados recebidos.
> a) O arquivo `json-20230618.jar` está localizado na pasta `/lib` deste repositório.

> b) No IntelliJ, pressione `Ctrl + Alt + Shift + S` (Project Structure).

> c) Vá em **Libraries** > **+** > **Java** e selecione o arquivo `.jar` que está na pasta `/lib`.

> d) Certifique-se de que ele está marcado como **Compile** na aba **Dependencies** do módulo `backend`.

### 3. Versão do Java
* **SDK:** Oracle OpenJDK 22.
* **Language Level:** 22.

## 💻 Como Executar
a) Abra a classe `com.jweather.JWeatherApplication`.

b) Execute o método `main`.

c) No console, digite o nome da cidade desejada e pressione **Enter**.
