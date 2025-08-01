
> 📘 **Portuguese Version**  
> This document is the Portuguese version of the original project README.  
> Looking for the English version? [Click here  🇬🇧](./Readme.md)

# DEMOCRACIA 2.0 --> Projecto de Referência de SpringBoot

# UC: Construção de Sistemas de Software

## *Autores:*
###     Alexandre Müller 	- fc56343
###     Diogo Ramos 			- fc56308
###     Francisco Henriques 	- fc56348       
 
# Descrição:

### Dada a baixa taxa de abstenção nas últimas eleições (44% em 2015, 51% em 2019 e 49% em 2022 1) e  devido à insatisfação da população com a sua representação política, a Comissão Nacional de Eleições está a estudar um modelo alternativo de representatividade. A sua equipa foi escolhida para fazer o desenvolvimento da plataforma teste.

No modelo proposto, pretende-se que os eleitores tenham a hipótese de votar directamente em todas as
propostas de lei que vão a votação actualmente no parlamento. Assim, cada cidadão poderá fazer-se ouvir
sobre cada assunto que lhe diz respeito. Este modelo é superior ao actual em virtude de que ao escolher um
representante, o cidadão está a atribuir todos os seus votos à(s) mesma(s) pessoa(s), independentemente
do tema. Com o novo modelo, o cidadão pode escolher uma sequência de votos que não corresponderia a
nenhum deputado, até mesmo os que não o representam. No entanto, ao perder o conceito de deputados, também se perde a vantagem dos cidadãos não terem de se
preocupar com todos os aspectos da governação e legislação. Muito do trabalho dos deputados é de desenho
de propostas, estudo para compreensão das mesmas e estudo do impacto que têm na vida dos portugueses.
De forma a manter a hipótese de delegar votos, é possível uma pessoa voluntariar-se como delegado (semelhante ao papel do actual deputado, mas sem eleições). Como delegado, os seus votos explicitos (i.e., que
não foram copiados de outros delegados) passam a ser públicos. No entanto passa a existir a possibilidade
de qualquer cidadão delegar os seus votos a um delegado, seja na totalidade, ou por tema (saúde, educação,
obras públicas, imigração, etc...).

### Para isso foi pedido a implementação de um Software que faça a gestão dos eleitores, delegados, propostas e votos. Este sistema precisa de satisfazer certas propriedades como: segurança, privacidade, escalabilidade de performance (temos 10 milhões de habitantes com várias propostas por dia!), integração com outros sistemas do governo, etc...


# Requisitos Funcionais (Casos de Uso):
(Existiam 3 casos de uso anteriores relativos à integração com sistemas de autorização do governo português mas como não foram aprovados
não serão implementados)

D. (F1) Listar as votações em curso. Este caso de uso permite obter uma listagem das propostas de lei
em votação neste momento.

E. (F1) Apresentar um projecto de lei. Neste caso de uso, um delegado poderá propor um projecto
de lei. Um projecto de lei é constituído por um título, um texto descriptivo, um anexo PDF com o
conteúdo principal do projecto de lei, uma data e hora de validade (máximo de um ano), um tema
associado e o delegado proponente.

F. (F1) Fechar projectos de lei expirados. Todos os projectos de lei cuja data limite já tenha decorrido
deverão ser fechados, sendo que não podem receber mais nenhuma assinatura.

G. (F1) Consultar projectos de lei. Deve ser possível listar e consultar os projectos de lei não expirados.

H. (F1) Apoiar projectos de lei. Cada projecto de lei pode ser apoiado por cidadãos (no limite de 1 apoio
por cidadão). Quando um projecto de lei tiver pelo menos 10.000 apoios, é criada uma votação para
esse projecto de lei imediatamente, com uma data de fecho igual à data de expiração do projecto de
lei, com um limite mínimo de 15 dias e um limite máximo de 2 meses. Ao abrir a votação, é lançado
automàticamente o voto do delegado proponente, como favorável.

I. (F1) Escolher delegado. Um cidadão pode escolher vários delegados, mas apenas um para cada
tema. Ou seja, pode ter um delegado para o tema de Saúde, um para Educação e um para outros
temas. Quando fecha uma votação onde o cidadão não votou, é feito um voto automático com base
no delegado do tema mais específico da proposta de lei.

J. (F1) Votar numa proposta. Um cidadão deve pedir uma listagem das votações e escolher a que lhe
interessa. Deverá ver qual o voto por omissão caso não o faça explicitamente (isto é, o voto do seu
delegado para o tópico), caso este esteja disponível. Caso não concorde, o cidadão poderá lançar o seu
voto (favorável ou desfavorável, não existem votos em branco nem nulos) numa proposta concreta.
Deve ser verificado se o cidadão já votou nesta proposta em concreto, mas não deve ser registado em
que opção foi o voto (dica: poderá ter de dividir o voto em duas partes, a do cidadão e a do conteúdo).
Se o cidadão for também um delegado, então deverá ser registado um voto público, que qualquer um
poderá confirmar.

K. (F1) Fechar uma votação. Assim que termina o prazo de uma votação, são atribuídos os votos dos
delegados para cada cidadão que não tenha votado explicitamente. Depois são contados os votos,
e se mais de metade dos votos forem favoráveis, então a proposta é fechada como aprovada. Caso
contrário é fechada como rejeitada.


# Requisitos Não Funcionais

• Os votos dos não delegados deverão ser secretos. A base de dados não deverá guardar qual foi o voto
de cada cidadão, apenas que votou, e o total dos votos favoráveis e desfavoráveis.
• Toda a informação deverá ser armazenada numa base de dados relacional.
• A plataforma deverá ser implementada em Spring Boot, de forma a ter um custo de desenvolvimento
baixo, e a usar a linguagem Java, que é a linguagem para a qual é mais fácil de contratar engenheiros.
• A camada de dados deverá usar JPA e Spring Data.
• A camada de negócios deverá usar o Domain Model, com meta-dados suficientes para que o JPA faça
o mapeamento para a base de dados.
• Deverá ser exposta uma API REST que os clientes (web ou mobile) poderão usar para interagir com a
aplicação.
• O repositório deverá aceitar apenas código com o nível de qualidade aceite pela equipa (usando o
pre-commit).
• O projecto deverá correr dentro de um docker, que será a forma como vai ser deployed no servidor de
produção.
• O controlo da participação de cada membro da equipa será feita através da actividade no repositório
git.


# Como executar o projeto:

## Primeiro passo

Correr o script `setup.sh`.

## Segundo passo 

Correr o script `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres.

## Terceiro passo

### Caso queira utlizar a aplicação WEB:
    Abrir http://localhost:8080 ;

### Caso queira utlizar a aplicação Desktop (que utiliza uma api REST):
    Correr o comando : "mvn clean javafx:run" 


