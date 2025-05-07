package utils;

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
     * Verifica se a entidade colide com algum tile sólido com base em sua direção e
     * velocidade.
     * Caso a colisão seja detectada, define a flag `entity.collisionOn = true`.
     *
     * @param entity A entidade em movimento (jogador, NPC, etc.)
     */
    public void checkTile(Entity entity) {
        // Calcula a posição absoluta dos limites da hitbox da entidade no mundo
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Converte as coordenadas do mundo para índices de coluna e linha do mapa
        int entityLeftCol = entityLeftWorldX / Config.TILE_SIZE;
        int entityRightCol = entityRightWorldX / Config.TILE_SIZE;
        int entityTopRow = entityTopWorldY / Config.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / Config.TILE_SIZE;

        // Detecta possíveis colisões com base na direção de movimento
        // Para cada direção, calcula-se a próxima linha ou coluna afetada
        // Verifica-se então se os dois tiles na borda da hitbox possuem colisão
        switch (entity.direction) {
            case Direction.UP -> {
                entityTopRow = (entityTopWorldY - entity.speed) / Config.TILE_SIZE;
                checkTileCollision(gp, entity, entityLeftCol, entityTopRow, entityRightCol, entityTopRow);
            }
            case Direction.DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / Config.TILE_SIZE;
                checkTileCollision(gp, entity, entityLeftCol, entityBottomRow, entityRightCol, entityBottomRow);
            }
            case Direction.LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / Config.TILE_SIZE;
                checkTileCollision(gp, entity, entityLeftCol, entityTopRow, entityLeftCol, entityBottomRow);
            }
            case Direction.RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.speed) / Config.TILE_SIZE;
                checkTileCollision(gp, entity, entityRightCol, entityTopRow, entityRightCol, entityBottomRow);
            }
        }
    }

    /**
     * Verifica colisão entre uma entidade e os objetos do mapa.
     * Utiliza a direção e velocidade da entidade para prever colisões.
     *
     * @param entity A entidade em movimento (jogador ou NPC).
     * @param player Define se a entidade é o jogador (usado para retorno de
     *               índice).
     * @return Índice do objeto colidido (se for o jogador); caso contrário, 999.
     */
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // Calcula posição absoluta das hitboxes
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                // Define deslocamento baseado na direção da entidade
                int deltaX = 0;
                int deltaY = 0;
                switch (entity.direction) {
                    case Direction.UP -> deltaY = -entity.speed;
                    case Direction.DOWN -> deltaY = entity.speed;
                    case Direction.LEFT -> deltaX = -entity.speed;
                    case Direction.RIGHT -> deltaX = entity.speed;
                }

                // Aplica deslocamento simulado para prever colisão
                entity.solidArea.x += deltaX;
                entity.solidArea.y += deltaY;

                // Verifica interseção com o objeto
                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                // Restaura posições originais das hitboxes
                entity.resetSolidArea();
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    /**
     * Verifica se os tiles nas posições fornecidas possuem colisão.
     * Caso positivo, define a flag `entity.collisionOn = true`.
     *
     * @param gp     Instância do painel do jogo, usada para acessar o gerenciador
     *               de tiles.
     * @param entity Entidade em análise, cuja flag de colisão pode ser ativada.
     * @param col1   Primeira coluna a ser verificada.
     * @param row1   Primeira linha correspondente.
     * @param col2   Segunda coluna a ser verificada.
     * @param row2   Segunda linha correspondente.
     */
    private void checkTileCollision(GamePanel gp, Entity entity, int col1, int row1, int col2, int row2) {
        // Obtém os índices dos tiles nas posições especificadas
        int tileNum1 = gp.getTileManager().getTileNumber(col1, row1);
        int tileNum2 = gp.getTileManager().getTileNumber(col2, row2);

        // Ativa a colisão se qualquer um dos tiles for sólido
        if (gp.getTileManager().hasCollision(tileNum1) || gp.getTileManager().hasCollision(tileNum2)) {
            entity.collisionOn = true;
        }
    }
}
