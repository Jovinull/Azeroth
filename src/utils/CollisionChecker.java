package utils;

import entity.Direction;
import entity.Entity;
import main.Config;
import main.GamePanel;

/**
 * Responsável por verificar colisões entre entidades e tiles do mapa.
 * Utiliza as propriedades de colisão dos tiles para determinar se uma entidade
 * pode ou não se mover para determinada direção.
 */
public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Verifica se a entidade colide com algum tile bloqueado na direção atual.
     * Se houver colisão, define `entity.collisionOn = true`.
     *
     * @param entity A entidade cujo movimento será verificado
     */
    public void checkTile(Entity entity) {
        // Posição absoluta da área sólida da entidade
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Conversão de coordenadas para colunas e linhas do mapa
        int entityLeftCol = entityLeftWorldX / Config.TILE_SIZE;
        int entityRightCol = entityRightWorldX / Config.TILE_SIZE;
        int entityTopRow = entityTopWorldY / Config.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / Config.TILE_SIZE;

        int tileNum1, tileNum2;

        // ======================================================================
        // Verificação de colisão com tiles sólidos, baseada na direção do movimento.
        // ======================================================================
        // Para detectar colisões, verificamos os tiles nas extremidades da área sólida
        // da entidade (solidArea) na direção em que ela pretende se mover.
        //
        // A lógica funciona da seguinte forma:
        // 1. Calcula-se a próxima linha/coluna que a entidade ocupará após o movimento,
        // considerando sua velocidade atual (entity.speed).
        // 2. Identificam-se dois tiles nas extremidades laterais (ou verticais) da
        // área sólida da entidade, conforme a direção (ex: topo esquerdo e topo direito
        // para cima).
        // 3. Para cada tile identificado, consulta-se se ele possui a flag `collision =
        // true`.
        // 4. Se qualquer um dos dois tiles tiver colisão, a flag `entity.collisionOn` é
        // ativada.
        //
        // Este mecanismo evita que a entidade atravesse paredes ou obstáculos sólidos.
        // ======================================================================
        switch (entity.direction) {
            case Direction.UP:
                // Projeta a linha do topo da entidade considerando o movimento para cima
                entityTopRow = (entityTopWorldY - entity.speed) / Config.TILE_SIZE;

                // Verifica os dois tiles onde os cantos esquerdo e direito da entidade
                // encostariam
                tileNum1 = gp.getTileManager().getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = gp.getTileManager().getTileNumber(entityRightCol, entityTopRow);

                // Se qualquer tile for sólido, ativa a flag de colisão
                if (gp.getTileManager().hasCollision(tileNum1) || gp.getTileManager().hasCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case Direction.DOWN:
                // Projeta a linha do fundo da entidade considerando o movimento para baixo
                entityBottomRow = (entityBottomWorldY + entity.speed) / Config.TILE_SIZE;

                // Verifica os dois tiles abaixo dos cantos esquerdo e direito da entidade
                tileNum1 = gp.getTileManager().getTileNumber(entityLeftCol, entityBottomRow);
                tileNum2 = gp.getTileManager().getTileNumber(entityRightCol, entityBottomRow);

                // Checagem de colisão semelhante
                if (gp.getTileManager().hasCollision(tileNum1) || gp.getTileManager().hasCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case Direction.LEFT:
                // Projeta a coluna da esquerda da entidade considerando o movimento para a
                // esquerda
                entityLeftCol = (entityLeftWorldX - entity.speed) / Config.TILE_SIZE;

                // Verifica os tiles que seriam tocados nos cantos superior e inferior
                tileNum1 = gp.getTileManager().getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = gp.getTileManager().getTileNumber(entityLeftCol, entityBottomRow);

                if (gp.getTileManager().hasCollision(tileNum1) || gp.getTileManager().hasCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;

            case Direction.RIGHT:
                // Projeta a coluna da direita da entidade considerando o movimento para a
                // direita
                entityRightCol = (entityRightWorldX + entity.speed) / Config.TILE_SIZE;

                // Verifica os tiles nos cantos superior e inferior do lado direito
                tileNum1 = gp.getTileManager().getTileNumber(entityRightCol, entityTopRow);
                tileNum2 = gp.getTileManager().getTileNumber(entityRightCol, entityBottomRow);

                if (gp.getTileManager().hasCollision(tileNum1) || gp.getTileManager().hasCollision(tileNum2)) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
}
