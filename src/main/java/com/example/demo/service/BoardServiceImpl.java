package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import com.example.demo.util.S3FileUtil;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardRepository repository;
	
//	@Autowired
//	FileUtil fileUtil;

	@Autowired
	S3FileUtil fileUtil;
	
	@Override
	public int register(BoardDTO dto) {
		Board entity = dtoToEntity(dto);
		
		// 사용자에게 전달받은 파일을 컴퓨터에 저장하고( 물리적으로 저장 )
		// 파일이름을 반환
		String imgPath = fileUtil.fileUpload(dto.getUploadFile());
		
		// 엔티티에 파일이름을 업데이트 ( db에 저장 )
		entity.setImgPath(imgPath);
		
		repository.save(entity);

		return entity.getNo();
	}

	@Override
	public List<BoardDTO> getList() {
		List<Board> entityList = repository.findAll();		
		List<BoardDTO> dtoList = entityList.stream()
				.map(entity -> entityToDto(entity))
				.collect(Collectors.toList());

		return dtoList;
	}

	@Override
	public BoardDTO read(int no) {
        Optional<Board> result = repository.findById(no);
        if(result.isPresent()) {
        	Board board =  result.get();
        	return entityToDto(board);
        } else {
        	return null;
        }
	}

	@Override
	public void modify(BoardDTO dto) {
        Optional<Board> result = repository.findById(dto.getNo());
        if(result.isPresent()){
            Board entity = result.get();
            entity.setTitle(dto.getTitle());
            entity.setContent(dto.getContent());
            repository.save(entity);
        }
	}

	@Override
	public void remove(int no) {
		repository.deleteById(no);
	}

}
