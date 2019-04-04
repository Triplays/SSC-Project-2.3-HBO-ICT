package GameAR;

import java.util.ArrayList;

public class Ruler
{

    private Board board;

    public Ruler(Board board)
    {
        this.board = board;
    }

    public Integer play(BoardPosition.Owner player, BoardPosition position) throws Exception
    {
        position.setOwner(player);

        Integer newPositionsAmount = 0;
        Boolean passedOpponent = false;

        // left
        BoardPosition workPos = position;
        while(this.board.hasLeftFrom(workPos)) {
            workPos = this.board.leftFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasRightFrom(workPos)) {
                    workPos = this.board.rightFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // right
        workPos = position;
        while(this.board.hasRightFrom(workPos)) {
            workPos = this.board.rightFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasLeftFrom(workPos)) {
                    workPos = this.board.leftFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // up
        workPos = position;
        while(this.board.hasUpFrom(workPos)) {
            workPos = this.board.upFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasDownFrom(workPos)) {
                    workPos = this.board.downFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // down
        workPos = position;
        passedOpponent = false;
        while(this.board.hasDownFrom(workPos)) {
            workPos = this.board.downFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                passedOpponent = true;
                continue;
            }

            if (workPos.getOwner() == player) {
                if (! passedOpponent) {
                    break;
                }

                while(this.board.hasUpFrom(workPos)) {
                    workPos = this.board.upFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // upper left
        workPos = position;
        while(this.board.hasUpperLeftFrom(workPos)) {
            workPos = this.board.upperLeftFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasDownRightFrom(workPos)) {
                    workPos = this.board.downRightFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // upper right
        workPos = position;
        while(this.board.hasUpperRightFrom(workPos)) {
            workPos = this.board.upperRightFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasDownLeftFrom(workPos)) {
                    workPos = this.board.downLeftFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // down left
        workPos = position;
        while(this.board.hasDownLeftFrom(workPos)) {
            workPos = this.board.downLeftFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasUpperRightFrom(workPos)) {
                    workPos = this.board.upperRightFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        // down right
        workPos = position;
        while(this.board.hasDownRightFrom(workPos)) {
            workPos = this.board.downRightFrom(workPos);

            if (workPos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (workPos.getOwner() == this.getOpponent(player)) {
                continue;
            }

            if (workPos.getOwner() == player) {
                while(this.board.hasUpperLeftFrom(workPos)) {
                    workPos = this.board.upperLeftFrom(workPos);

                    if (workPos.getOwner() == player) {
                        break;
                    }

                    workPos.setOwner(player);
                    newPositionsAmount++;
                }

                break;
            }
        }

        return newPositionsAmount;
    }

    public ArrayList<BoardPosition> getAvailableMoves(BoardPosition.Owner player) throws Exception
    {
        ArrayList<BoardPosition> positions = this.board.getPositions(player);
        ArrayList<BoardPosition> availableMoves = new ArrayList<>();

        for (int i = 0; i < positions.size(); i++) {
            BoardPosition pos = positions.get(i);

            Boolean passedOpponent = false;
            while(this.board.hasLeftFrom(pos)) {
                pos = this.board.leftFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasRightFrom(pos)) {
                pos = this.board.rightFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasUpFrom(pos)) {
                pos = this.board.upFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasDownFrom(pos)) {
                pos = this.board.downFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasUpperLeftFrom(pos)) {
                pos = this.board.upperLeftFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasUpperRightFrom(pos)) {
                pos = this.board.upperRightFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasDownLeftFrom(pos)) {
                pos = this.board.downLeftFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }

            pos = positions.get(i);
            passedOpponent = false;
            while(this.board.hasDownRightFrom(pos)) {
                pos = this.board.downRightFrom(pos);

                if (pos.getOwner() == player) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    if (passedOpponent && ! availableMoves.contains(pos)) {
                        availableMoves.add(pos);
                    }

                    break;
                }

                if (pos.getOwner() == this.getOpponent(player)) {
                    passedOpponent = true;
                }
            }
        }

        return availableMoves;
    }

    public BoardPosition.Owner getOpponent(BoardPosition.Owner player) {
        return player == BoardPosition.Owner.White ? BoardPosition.Owner.Black : BoardPosition.Owner.White;
    }

}
