#language: pt

Funcionalidade: Armazenamento de Imagens de Food Items
  Como sistema de armazenamento
  Eu quero gerenciar imagens de itens alimentares
  Para que possam ser salvas, recuperadas e excluídas do sistema de arquivos

  Cenário: Salvar uma imagem com sucesso
    Dado que tenho dados de imagem em base64
    E um nome de arquivo "burger.jpg"
    Quando eu salvar a imagem
    Então a imagem deve ser salva no diretório configurado
    E o arquivo deve existir no sistema de arquivos

  Cenário: Salvar múltiplas imagens
    Dado que tenho uma lista com 3 imagens
    Quando eu salvar todas as imagens
    Então todas as 3 imagens devem ser salvas no diretório

  Cenário: Recuperar dados de uma imagem existente
    Dado que existe uma imagem "pizza.jpg" salva no diretório
    Quando eu recuperar os dados da imagem "pizza.jpg"
    Então devo receber os dados em formato base64
    E os dados não devem estar vazios

  Cenário: Recuperar múltiplas imagens
    Dado que existem 2 imagens salvas no diretório
    Quando eu recuperar os dados de todas as imagens
    Então todas as imagens devem ter dados preenchidos

  Cenário: Tentar recuperar imagem inexistente
    Dado que a imagem "naoexiste.jpg" não existe no diretório
    Quando eu tentar recuperar os dados da imagem "naoexiste.jpg"
    Então uma exceção FoodItemException deve ser lançada
    E o código de erro deve ser 404

  Cenário: Deletar uma imagem existente
    Dado que existe uma imagem "delete-me.jpg" salva no diretório
    Quando eu deletar a imagem "delete-me.jpg"
    Então a imagem "delete-me.jpg" não deve mais existir no sistema de arquivos

  Cenário: Deletar múltiplas imagens
    Dado que existem 3 imagens para deletar
    Quando eu deletar todas as imagens
    Então nenhuma das imagens deve existir mais no sistema de arquivos

  Cenário: Tentar deletar imagem inexistente
    Dado que a imagem "inexistente.jpg" não existe
    Quando eu tentar deletar a imagem "inexistente.jpg"
    Então nenhuma exceção deve ser lançada

  Cenário: Criar diretório automaticamente ao salvar
    Dado que o diretório de imagens não existe
    E tenho dados de imagem válidos
    Quando eu salvar a imagem
    Então o diretório deve ser criado automaticamente
    E a imagem deve ser salva com sucesso

