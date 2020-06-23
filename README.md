# RedeNeural---MLP
A multilayer perceptron using java

Projeto desenvolvido para a matéria de Inteligência Artificial. <br>
O projeto conta com apenas uma camada oculta já pré definida, porém pode ser escolhido a quantidade de neurônios para ela. Por padrão, a quantidade será (NumeroDeAtributos + NumeroDeClasses) / 2 <br>
<br>
![alt text](https://github.com/riccihenrique/RedeNeural---MLP/blob/master/RedeNeural%20-%20MLP/src/arquivos/index.PNG)
<br>
As funções de ativação existentes no projeto são a Linear, Logistica e Tangente Hiperbólica, que podem ser aplicadas tanto na camada oculta como na camada de saída. <br> <br>
Os parametros "Taxa de Aprendizado", "Erro mínimo" e "Épocas" definem, respectivamente, o valor que será usado no cálculo da atualização dos pesos (entre 0 e 1), o valor mínimo do error para o treinamento finalizar e
a quantidade máxima de épocas que o treinamento irá ser executado. <br>

### Execução

Clique no botão "Carregar Arquivo". Ele pedirá para selecionar o CSV de treinamento e em seguida o de teste. Após a leituda de ambos os arquivos, serão realiadas as normalizações 
dos dados e o cálculo dos neurônios da camada oculta. <br>
<br>
![alt text](https://github.com/riccihenrique/RedeNeural---MLP/blob/master/RedeNeural%20-%20MLP/src/arquivos/readFile.PNG)
<br>
Para treinar, basta clicar no botão "Treinar" no canto inferior esquerdo. Será aberta uma nova tela. Ao final do treinamento, será exibido um gráfico contendo os erros da rede e 
uma tabela contendo as relações de predição/resultado esperado com os dados de teste. Também é exibido um gráfico com o percentual de acerto. <br>
<br>
![alt text](https://github.com/riccihenrique/RedeNeural---MLP/blob/master/RedeNeural%20-%20MLP/src/arquivos/train.PNG)
<br>

#### CRIADO EM CONJUNTO COM O BROTHER [Alexandre Oliveira](https://github.com/AlekOliveira)
