package objects;

import utils.ObjectImageUtils;

/**
 * Representa o item coletável "Boots" (botas) no jogo.
 * Ao ser coletado, aumenta a velocidade do jogador.
 */
public class OBJ_Boots extends SuperObject {

    /**
     * Construtor que define o nome e carrega a imagem associada ao objeto.
     * A imagem é carregada do caminho configurado nos recursos.
     */
    public OBJ_Boots() {
        name = "Boots";
        image = ObjectImageUtils.loadImage("/res/objects/boots.png");
    }
}
